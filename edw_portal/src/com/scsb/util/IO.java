package com.scsb.util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;

public class IO {
	private static Logger logger = Logger.getLogger(IO.class.getName());

	public static String read(String filepath) {
		return read(filepath,"");
	}
	
 	public String read_mod2(String filepath) {
		String sendxml = "";
		try {
			FileInputStream fis = new FileInputStream(filepath);
			int n;
			while ((n = fis.available()) > 0) {
				byte b[] = new byte[n];
				int result = fis.read(b);
				if (result == -1)
					break;
				sendxml = new String(b);
				logger.info(sendxml);
			}
			fis.close();
		} catch (FileNotFoundException e) {
			logger.error(StrUtil.convException(e));
		} catch (IOException e) {
			logger.error(StrUtil.convException(e));
		}
		return sendxml;
	}
	
	public static void writeFile(String filePath,String content) {
		try {			
			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			bw.write(content);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 讀檔.並指定語系
	 * @param path
	 * @param chartype utf-8|big5  不指定.則用本機語系
	 * @return
	 */
	public static String read(String path, String encoding) {
		FileReader fileReader;
		String rtnStr = "";
		try {
			fileReader = new FileReader(path);
			String encode = fileReader.getEncoding();
			if (encoding!=null && encoding.length()>0){
				encode = encoding;
			}
   
			FileInputStream fileInputStream = new FileInputStream(path);
			//logger.info("讀檔使用語系:"+encode);
			InputStreamReader inputStramReader = new InputStreamReader(fileInputStream, encode);
			
			BufferedReader bufferedReader = new BufferedReader(inputStramReader);
			int ch;
			StringBuffer buffer = new StringBuffer();
			while ((ch = bufferedReader.read()) > -1) {
				buffer.append((char) ch);
			}
			fileInputStream.close();
			inputStramReader.close();
			rtnStr = buffer.toString();
			fileReader.close();
						
		} catch (FileNotFoundException e) {
			logger.error(StrUtil.convException(e));
		} catch (UnsupportedEncodingException e) {
			logger.error(StrUtil.convException(e));
		} catch (IOException e) {
			logger.error(StrUtil.convException(e));
		} 
		return rtnStr;		
	}
	
	/**
	 * 將String透過getByte(encoding)來處理
	 * @param filename
	 * @param data
	 * @throws IOException
	 */
	public static void writeFile(String filename, byte[] data) throws IOException {
		File targetFile = new File(filename);
		boolean append = false;// append在檔案後面,從頭寫起
		FileOutputStream fos = new FileOutputStream(targetFile, append);
		fos.write(data);
		fos.close();	
	}
	
	/**
	 * 字串寫檔案
	 * @param filename
	 * @param data
	 * @param encoding
	 */
	public static void writeFile(String filename, String data, String encoding) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
		    OutputStreamWriter osw = new OutputStreamWriter(fos, encoding); 
		    osw.write(data); 
		    osw.flush(); 
		    osw.close();
		} catch (FileNotFoundException e) {
			logger.error(StrUtil.convException(e));
		} catch (UnsupportedEncodingException e) {
			logger.error(StrUtil.convException(e));
		} catch (IOException e) {
			logger.error(StrUtil.convException(e));
		} 
	}
	
	public static void moveFile(File file1, File file2) {
		try {
			copy(file1.getAbsolutePath(), file2.getAbsolutePath());
			file1.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized boolean deleteSubDir(File dir) {
		if (dir == null)
			return false;
		File files[] = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory())
				deleteDir(file);
			else
				file.delete();
		}

		return true;
	}

	public static synchronized boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String files[] = dir.list();
			for (int i = 0; i < files.length; i++) {
				boolean success = deleteDir(new File(dir, files[i]));
				if (!success)
					return false;
			}

		}
		return dir.delete();
	}

	public static synchronized void xcopy(String dirPath, String target) {
		File dir = new File(dirPath);
		File tFile = new File(target);
		String separator = File.separator;
		if (!tFile.exists())
			tFile.mkdirs();
		String contents[] = dir.list();
		for (int i = 0; i < contents.length; i++) {
			File fileOrDir = new File(dirPath, contents[i]);
			if (fileOrDir.isDirectory())
				xcopy(dirPath + separator + contents[i], target + separator
						+ contents[i]);
			else
				copy(fileOrDir.getPath(), target + separator + contents[i]);
		}

	}

	public static synchronized void copy(String source, String target) {
		try {
			FileInputStream fis = new FileInputStream(source);
			FileOutputStream fos = new FileOutputStream(target);
			byte buffer[] = new byte[4096];
			int total = 0;
			int bytes_readed;
			while ((bytes_readed = fis.read(buffer)) != -1)
				fos.write(buffer, 0, bytes_readed);
			fos.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isExist(String fullpath){
		File f = new File(fullpath);
		return f.exists();
	}
	
	public static void initialPath(String targetDirPath, boolean deleteFlag, String[] exclude) {
		File path = new File(targetDirPath);
		
		if( path.exists() ) {
			File[] files = path.listFiles();
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					initialPath(files[i].getAbsolutePath(), false, null);
					System.out.println("檔案刪掉囉");
					//if (deleteFlag)
					//	files[i].delete();
				} else {
					if (deleteFlag) {
						if (exclude!=null && exclude.length>0) {
							for (int j=0; j<exclude.length; j++){
								if (files[i].getName().startsWith(exclude[j]))
									System.out.println("指定不刪檔案:"+exclude[j]);
								else {
									System.out.println("檔案刪掉囉");
									//files[i].delete();
								}
							}					
						} else {
							System.out.println("檔案刪掉囉");							
							//files[i].delete();						
						}						
					}	
				}								
			}
		} else {
			if (path.getParentFile().exists()) 
				path.mkdirs();
			else {
				initialPath(path.getParentFile().getAbsolutePath(),false,null);
				if (path.getParentFile().exists() && !path.exists()) 
					path.mkdirs();				
			}
		}
	}	
	
	/**
     * 輸入資料夾的路徑, 顯示得該資料夾下的所有檔案
     * @param String folderPath
     * @author Lupin
     **/
    public static HashSet getFileList(String folderPath){
        //String folderPath = "C:\\";//資料夾路徑
    	HashSet hashSet =new HashSet();
        try{
           java.io.File folder = new java.io.File(folderPath);
           String[] list = folder.list();          
           for(int i = 0; i < list.length; i++){
        	   hashSet.add(list[i]);
           }
        }catch(Exception e){
            System.out.println("'"+folderPath+"'此資料夾不存在");
        }
        return hashSet;
    }
    
    /**
    * 複製單個檔
    * @param oldPath String原檔路徑 如：c:/fqf.txt
    * @param newPath String複製後路徑 如：f:/fqf.txt
    * @return boolean
    */
    public static synchronized void copyFile(String oldPath, String newPath) {
	    try {
	    	int bytesum = 0;
	    	int byteread = 0;
	    	File oldfile = new File(oldPath);
	    	if (oldfile.exists()) { //檔存在時
	    			InputStream inStream = new FileInputStream(oldPath);//讀入原檔
	    			FileOutputStream fs = new FileOutputStream(newPath);
	    			byte[] buffer = new byte[1024];
	    			while ( (byteread = inStream.read(buffer)) != -1) {
	    					bytesum += byteread; //位元組數 檔案大小
	    					//System.out.println(bytesum);
	    					fs.write(buffer, 0, byteread);
	    			}
	    			inStream.close();
	    			fs.close();
	    	}
	    }catch(Exception e) {
		    System.out.println("複製單個檔操作出錯");
		    e.printStackTrace();
	    }
    } 
    
    /**
     * 建立資料夾(可建多層資料夾)
     * @param path
     * @param 最後一層的資料夾
     */
    public static String mkDir(String path){
        String [] pathAry = path.split("[/]|\\\\");

        StringBuffer list = new StringBuffer();
        for(int i = 0; i < pathAry.length; i++){
            if(!pathAry[i].equals("")){
                list.append(pathAry[i] + "/");
                File dir = new File(list.toString());
                //System.out.println(dir.getName());
                if (!dir.isDirectory()){
                    dir.mkdir();

                }
            }
        }
        return list.toString();
    }    
		
	public static void main(String[] args){
		String x = IO.read("C:/TEMP/sc/send/xml/201304030001.xml", "big5");
		System.out.println(x);
	}
	
	
	
}
