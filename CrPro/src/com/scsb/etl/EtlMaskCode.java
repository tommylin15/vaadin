package com.scsb.etl;

import java.util.Hashtable;
import java.util.Iterator;

import org.vaadin.teemu.jsoncontainer.JsonContainer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.scsb.util.INIReader;
import com.scsb.util.IO;
import com.scsb.util.LogWriter;
import com.scsb.util.MaskUtil;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

public class EtlMaskCode extends VerticalLayout{
	
	protected static LogWriter log;
	
	public EtlMaskCode(String configPath){
		String path=configPath;		
		try {
			log=new LogWriter(path+"/log/MASK_Batch"+LogWriter.GetDate()+".log");
			log.append("=================EtlMaskCode Running ....=================",true);
			//載入 dbconnection.ini
			INIReader dbini =new INIReader(path+"/DBconnection.INI");			
			//載入config.json
			log.append("載入config.json ....",true);
			String config =IO.read(path+"/config.json");
			JsonContainer configData= JsonContainer.Factory.newInstance(config);	
			for(Iterator iterConfig= configData.getItemIds().iterator();iterConfig.hasNext();){
				Item itemConfig=configData.getItem(iterConfig.next());
				String sidConfig=(String)itemConfig.getItemProperty("sid").getValue();
				String isRunConfig=(String)itemConfig.getItemProperty("isRun").getValue();
				if (isRunConfig.equals("Y")){
					//載入config_XXX.json
					log.append("載入config_"+sidConfig+".json ....",true);
					String jsonString =IO.read(path+"/config_"+sidConfig+".json");
					JsonContainer jsonData= JsonContainer.Factory.newInstance(jsonString);	
					log.append("載入DBconnection.INI ....",true);
					for(Iterator iter= jsonData.getItemIds().iterator();iter.hasNext();){
						log.append("=============================================",true);
						Item item=jsonData.getItem(iter.next());
			        	String sid=(String)item.getItemProperty("sid").getValue();
			        	String idb=(String)item.getItemProperty("idb").getValue();
			        	String odb=(String)item.getItemProperty("odb").getValue();
			        	log.append("sid:"+sid,true);
			        	log.append("idb:"+idb,true);
			        	log.append("odb:"+odb,true);
			        	
			        	Connection conIn =null;
			        	Connection conOut =null;
			        	
						try {
				    		String classForName1=dbini.Read(idb, "Class.forName").trim();
				    		String driverManager1=dbini.Read(idb, "DriverManager").trim();
				    		String id1=dbini.Read(idb, "id").trim();
				    		String password1=dbini.Read(idb, "password").trim();
				    		
				    		String classForName2=dbini.Read(odb, "Class.forName").trim();
				    		String driverManager2=dbini.Read(odb, "DriverManager").trim();
				    		String id2=dbini.Read(odb, "id").trim();
				    		String password2=dbini.Read(odb, "password").trim();	   
				    		
							//log.append("classForName in:"+classForName1,true);
							//log.append("driverManager in:"+driverManager1,true);
							Class.forName(classForName1);
							conIn   =   DriverManager.getConnection(driverManager1      ,id1      ,password1);
							
							//log.append("classForName out:"+classForName2,true);
							//log.append("driverManager out:"+driverManager2,true);					
							Class.forName(classForName2);
							conOut   =   DriverManager.getConnection(driverManager2      ,id2      ,password2);
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							log.append(e1.getMessage(),true);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.append(e.getMessage(),true);
						}
			        	chooseDB(path+"/"+sid+".INI" ,conIn ,conOut );
						try {
							if(conIn!=null)	conIn.close();
							if(conOut!=null)	conOut.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.append(e.getMessage(),true);
						}
					}//for		
				}//if
			}//for
		} catch (IOException e1) {
			e1.printStackTrace();
			log.append(e1.getMessage(),true);
		}

	}

	/**
	 * 讀取 *.INI檔進行DATA MASK
	 * @param iniFile
	 * @param conIn
	 * @param conOut
	 * @param iMaxRows
	 * @param iWriteRows
	 */
	private void chooseDB(String iniFile ,Connection conIn ,Connection conOut ){
		log.append("chooseDB:"+iniFile,true);
		INIReader ini =new INIReader(iniFile);
		int tables=Integer.parseInt(ini.Read("DB", "tables").trim());
		for(int i=1 ;i<=tables;i++){
			String fromTable=ini.Read("TABLE", "fromTable"+i).trim();
			String toTable=ini.Read("TABLE", "toTable"+i).trim();
			 
			String fieldsC=ini.Read(toTable ,"fieldsC").trim();
			String fieldsE=ini.Read(toTable ,"fieldsE").trim();
			String isRun=ini.Read(toTable ,"isRun").trim();
			int maxSize=Integer.parseInt(ini.Read(toTable ,"maxSize").trim());
			int oneBatch=Integer.parseInt(ini.Read(toTable ,"oneBatch").trim());
			
			log.append("isRun:"+isRun,true);
			if (isRun.toUpperCase().trim().equals("Y")){
				log.append("Query From Table:"+fromTable,true);
				log.append("Insert To Table:"+toTable,true);
				log.append("fields mask Type C :"+fieldsC,true);
				log.append("fields mask Type E :"+fieldsE,true);
				try {
					QueryDB(conIn ,fromTable  ,conOut  ,toTable ,fieldsC ,fieldsE ,oneBatch ,maxSize);
				} catch (SQLException e) {
					log.append(e.getMessage(),true);
				}
			}
		}
	}
	
	/**
	 * 取得要轉換的資料...之後依設定筆數,分批進行處理
	 * @param conIn
	 * @param fromTable
	 * @param conOut
	 * @param toTable
	 * @param fieldsC
	 * @param fieldsE
	 * @param iWriteRows
	 * @param maxSize (0 = 全部) 
	 * @throws SQLException
	 */
	private boolean QueryDB(Connection conIn ,String fromTable
			 ,Connection conOut ,String toTable ,String fieldsC ,String fieldsE
			,int iWriteRows ,int maxSize
			) throws SQLException {
		
		boolean isRtn=true;
		conOut.setAutoCommit(true);
        try {
        	//設定每次跑幾筆
        	int iPage=iWriteRows;
        	
		    //先清空,再新增 ,TRUNCATE TABLE &nowfile.
    		String delSQL =" delete from "+toTable;
    	    conOut.prepareStatement(delSQL).executeUpdate();
    	    log.append("sql:"+delSQL,true);
        	//分頁讀table************************************************************************************
			String sql =" select * from "+fromTable;
			log.append("sql:"+sql,true);
			PreparedStatement prstmt = conIn.prepareStatement(sql);
			System.out.println("QueryTimeout:"+prstmt.getQueryTimeout());
			if (maxSize != 0) prstmt.setMaxRows(maxSize);
			ResultSet rs= prstmt.executeQuery();
			
			ResultSetMetaData  rsMeta =rs.getMetaData();
			IndexedContainer container=getContainerFmt(rsMeta);
    		int iRows=0;
    		int iTotRows=0;
        	while(rs.next()){
				iRows++;
				iTotRows++;
				Item item =container.addItem(iRows);
				for(int i = 1; i <= rsMeta.getColumnCount(); i++){
					String fieldName=rsMeta.getColumnName(i).toUpperCase();
					String fieldType =rsMeta.getColumnTypeName(i).toUpperCase();
					if (fieldType.equals("DATE"))
						item.getItemProperty(fieldName).setValue(rs.getDate(i));
					else if (fieldType.equals("NUMBER"))
						item.getItemProperty(fieldName).setValue(rs.getDouble(i));
					else if (fieldType.equals("INT"))
						item.getItemProperty(fieldName).setValue(rs.getInt(i));
					else if (fieldType.equals("INTEGER"))
						item.getItemProperty(fieldName).setValue(rs.getInt(i));
					else if (fieldType.equals("FLOAT"))
						item.getItemProperty(fieldName).setValue(rs.getFloat(i));
					else if (fieldType.equals("TIMESTAMP"))
						item.getItemProperty(fieldName).setValue(rs.getTimestamp(i));
					else if (fieldType.equals("DOUBLE"))
						item.getItemProperty(fieldName).setValue(rs.getDouble(i));
					else if (fieldType.equals("LONG")){
						item.getItemProperty(fieldName).setValue(rs.getString(i));
					}else if (fieldType.equals("LONGVARCHAR"))
						item.getItemProperty(fieldName).setValue(rs.getString(i));
					else {
						String sObj=rs.getString(i);
						if (sObj != null){
							if (sObj.length() >1000) sObj=sObj.substring(0,1000);
						}
						
						item.getItemProperty(fieldName).setValue(sObj);
					}
				}
				
				if (iRows >=iPage){
					//每iPage筆寫入db一次
					isRtn=maskTable( container ,conOut ,toTable ,fieldsC ,fieldsE);
				    log.append(toTable+"'s writting :"+iTotRows,true);
				    iRows=0;
				    container=getContainerFmt(rsMeta);
				    //等java釋放記憶體太慢,所以自己主動釋放
				    //log.append("System.gc().........................",true);
			    	//System.gc();
			    	if (!isRtn)  		break;
				}
        	}//while
        	if (iRows > 0 && isRtn){
        		isRtn=maskTable( container ,conOut ,toTable ,fieldsC ,fieldsE);
			    log.append(toTable+"'s writting :"+iTotRows,true);        		
        	}
        	log.append(fromTable +" 轉檔至 "+toTable+" 結束,總筆數:"+iTotRows,true);
	   // Handle any SQL errors			
	    } catch (SQLException se) {
	    	System.out.println("QueryDB SQL errors	...");
	    	log.append(se.getMessage(),true);
	    	isRtn =false;
	    }
        if (isRtn) conOut.commit();
        else conOut.rollback();
        conOut.setAutoCommit(false);
		return isRtn;	
	}		
	
	/**
	 * 建立Container的格式
	 * @param rsMeta
	 * @return
	 * @throws SQLException
	 */
	private IndexedContainer getContainerFmt(ResultSetMetaData  rsMeta) throws SQLException{
    	int numCols=0;        	
		numCols = rsMeta.getColumnCount();
		Object[] oRow =new Object[numCols];
		Hashtable<String,String> hLabel =new Hashtable<String,String>();
		
		IndexedContainer      container  = new IndexedContainer();
    	for (int i=1 ;i<=numCols ;i++){
    		String fieldName=rsMeta.getColumnName(i).toUpperCase();
    		String filedLabel =rsMeta.getColumnLabel(i)+"";
    		if (filedLabel.equals("") || filedLabel.equals("null") || filedLabel.equals("NULL") ) filedLabel=fieldName;
    		
			String fieldType =rsMeta.getColumnTypeName(i).toUpperCase();
			if (fieldType.equals("DATE"))
				container.addContainerProperty(fieldName, Date.class,  null);
			else if (fieldType.equals("NUMBER"))
				container.addContainerProperty(fieldName, Double.class,  null);
			else if (fieldType.equals("INT"))
				container.addContainerProperty(fieldName, Integer.class,  null);				
			else if (fieldType.equals("INTEGER"))
				container.addContainerProperty(fieldName, Integer.class,  null);				
			else if (fieldType.equals("FLOAT"))
				container.addContainerProperty(fieldName, Float.class,  null);				
			else if (fieldType.equals("TIMESTAMP"))
				container.addContainerProperty(fieldName, Timestamp.class,  null);
			else if (fieldType.equals("DOUBLE"))
				container.addContainerProperty(fieldName, Double.class,  null);
			else if (fieldType.equals("LONG"))
				container.addContainerProperty(fieldName, String.class,  null);
			else 
				container.addContainerProperty(fieldName, String.class,  null);				
			
    		oRow[i-1] =fieldName;
    		hLabel.put(fieldName, filedLabel);
    	}	
    	return container;
	}
	
	/**
	 * 建立寫檔規格,之後傳到 batchInsert(...) 進行data mask及整批寫入
	 * @param container
	 * @param conOut
	 * @param toTable
	 * @param fieldsC
	 * @param fieldsE
	 * @return
	 */
	private boolean maskTable( IndexedContainer container  ,Connection conOut 
			 ,String toTable ,String fieldsC ,String fieldsE){
		boolean isRtn=false;
		try {
			if (container.size() > 0){
				Item item=container.getItem(container.getIdByIndex(0));
				String insertSQL1=" insert into "+toTable +"(";
				String insertSQL2=" values(";
				int iflag=0;
				for(Iterator iter=item.getItemPropertyIds().iterator();iter.hasNext();){
					String fieldName =(String)iter.next();
					Object obj=item.getItemProperty(fieldName).getValue();
					if (fieldName.equals("AUDIT")) fieldName="\"AUDIT\"";
					if (iflag==0){
						insertSQL1+=" "+fieldName;
						insertSQL2+=" ?";
					}else{
						insertSQL1+=" ,"+fieldName;
						insertSQL2+=" ,?";
					}
					iflag++;
				}//FOR
				insertSQL1+=" ) ";
				insertSQL2+=" ) ";
				 //log.append("batchInsert:"+insertSQL1+insertSQL2,true);
				 isRtn=batchInsert(conOut ,container  ,insertSQL1+insertSQL2  ,fieldsC ,fieldsE );
			}//if		
		} catch (SQLException se) {
			System.out.println("maskTable SQL errors	...");
	    	log.append(se.getMessage(),true);
			se.printStackTrace();
			isRtn=false;
		}
		return isRtn;
	}
	
	/**
	 * 資料整批寫入 (需要setAutoCommit(false))
	 * @param conOut
	 * @param container
	 * @param insertSQL
	 * @param fieldsC
	 * @param fieldsE
	 * @return
	 * @throws SQLException
	 */
	private boolean batchInsert(Connection conOut ,IndexedContainer container 
			 											 ,String insertSQL  ,String fieldsC ,String fieldsE) throws SQLException{		
		PreparedStatement pstmt =null;
		boolean isRtn=false;
		try{
		     pstmt = conOut.prepareStatement(insertSQL);
		     for(int i=1; i<= container.size();i++){
		    	 Item item=container.getItem(container.getIdByIndex(i-1));
		    	 int iFlag=0;
		    	 for(Iterator iter=item.getItemPropertyIds().iterator();iter.hasNext();){
						String fieldName =(String)iter.next();
						iFlag++;
						if (fieldsE.indexOf("'"+fieldName.trim()+"'") > -1){
							String obj=(String)item.getItemProperty(fieldName).getValue();
							obj=MaskUtil.maskCode(obj);
							pstmt.setString(iFlag,obj);
						}else if (fieldsC.indexOf("'"+fieldName.trim()+"'") > -1){
							String obj=(String)item.getItemProperty(fieldName).getValue();
							if (obj != null){
								int iLen=obj.length();
								if (iLen >2) obj=obj.substring(0,2)+"xxxx";
								if (iLen <= 6) obj=obj.substring(0,iLen);
							}
							pstmt.setString(iFlag,obj);
						}else{
							String fieldType=item.getItemProperty(fieldName).getType().getSimpleName().toUpperCase();
//System.out.println(fieldName+" write field type:"+fieldType);
							Object obj =item.getItemProperty(fieldName).getValue();
							if (fieldType.equals("DATE"))
								pstmt.setDate(iFlag,(Date)obj);
							else if (fieldType.equals("NUMBER"))
								pstmt.setDouble(iFlag,(Double)obj);
							else if (fieldType.equals("INT"))
								pstmt.setInt(iFlag,(Integer)obj);
							else if (fieldType.equals("INTEGER"))
								pstmt.setInt(iFlag,(Integer)obj);
							else if (fieldType.equals("FLOAT"))
								pstmt.setFloat(iFlag,(Float)obj);
							else if (fieldType.equals("TIMESTAMP"))
								pstmt.setTimestamp(iFlag,(Timestamp)obj);
							else if (fieldType.equals("DOUBLE"))
								pstmt.setDouble(iFlag,(Double)obj);
							else if (fieldType.equals("LONG"))
								pstmt.setLong(iFlag,(Long)obj);
							else 
								pstmt.setObject(iFlag,obj);
						}
					}//FOR
			        pstmt.addBatch();		    	 
		     }//for
		     pstmt.executeBatch();
			 isRtn=true;
		}catch(Exception se){
		     se.printStackTrace();
		    log.append(se.getMessage(),true);
		    isRtn=false;
		} finally{
		     if(pstmt!=null)    pstmt.close();
		}
		 return isRtn;
	}	
	
	static public void main(String[] argv){
		String path="D:/application/GitHome/git/CrPro/WebContent/properites/makecode";
		EtlMaskCode runCode =new EtlMaskCode(path);
	}
}
