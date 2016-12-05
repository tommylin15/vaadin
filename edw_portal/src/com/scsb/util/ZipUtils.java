package com.scsb.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtils {

	/**
	 * 解壓縮
	 * @param zipfile		zip檔位置
	 * @param extractDir	解壓縮資料夾
	 * @return
	 */
	    public boolean unzipFile(File zipfile, File extractDir ,LogWriter log ){
		
	        try {
				unZip(zipfile, extractDir.getAbsolutePath() ,log);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.append(e.getMessage(),true);
				e.printStackTrace();
				return false;
			}
			
	    	return true;
		}
	    
	    /** 
	     * Uncompress the incoming file. 
	     * @param inFileName Name of the file to be uncompressed 
	     */ 
	    public void unGzipFile(File zipfile, String ungzipFile ,LogWriter log ){ 
	        try { 
	            System.out.println("Opening the compressed file.");
	            GZIPInputStream in = null; 
	            try { 
	                in = new GZIPInputStream(new FileInputStream(zipfile)); 
	            } catch(FileNotFoundException e) { 
	                System.err.println("File not found. " + zipfile); 
	                System.exit(1); 
	            } 

	            System.out.println("Open the output file."); 
	            FileOutputStream out = null; 
	           try { 
	                out = new FileOutputStream(ungzipFile); 
	            } catch (FileNotFoundException e) { 
	                System.err.println("Could not write to file. " + ungzipFile); 
	                System.exit(1); 
	            } 

	            System.out.println("Transfering bytes from compressed file to the output file."); 
	            byte[] buf = new byte[1024]; 
	            int len; 
	            while((len = in.read(buf)) > 0) { 
	                out.write(buf, 0, len); 
	            } 

	            System.out.println("Closing the file and stream"); 
	            in.close(); 
	            out.close(); 

	        } catch (IOException e) { 
	            e.printStackTrace(); 
	            System.exit(1); 
	        } 

	    } 
 
	    
	    /**
	     * 建立資料夾
	     * @param directory
	     * @param subDirectory
	     */
	   private void createDirectory(String directory, String subDirectory ,LogWriter log ) {
		    String dir[];
		    File fl = new File(directory);
		    try {
		      if (subDirectory == "" && fl.exists() != true)
		        fl.mkdir();
		      else if (subDirectory != "") {
		        dir = subDirectory.replace('\\', '/').split("/");
		        for (int i = 0; i < dir.length; i++) {
		          File subFile = new File(directory + File.separator + dir[i]);
		          if (subFile.exists() == false)
		            subFile.mkdir();
		          directory += File.separator + dir[i];
		        }
		      }
		    }
		    catch (Exception ex) {
		      log.append(ex.getMessage(),true);
		      //System.out.println(ex.getMessage());
		    }
		  }
	   /**
	    * 解壓縮主程式
	    * @param zipFileName
	    * @param outputDirectory
	    * @throws Exception
	    */
		public void unZip(File ZIPFile, String outputDirectory ,LogWriter log ) throws Exception {
		    try {
		      org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(ZIPFile);
		      java.util.Enumeration e = zipFile.getEntries();
		      org.apache.tools.zip.ZipEntry zipEntry = null;
		      createDirectory(outputDirectory, "" ,log);
		      //if(!outputDirectory.exists())	outputDirectory.mkdirs();
		      
		      while (e.hasMoreElements()) {
		        zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();
		        System.out.println("unziping " + zipEntry.getName());
		        if (zipEntry.isDirectory()) {
		          String name = zipEntry.getName();
		          name = name.substring(0, name.length() - 1);
		          File f = new File(outputDirectory + File.separator + name);
		          f.mkdir();
		          log.append("創建立目錄：" + outputDirectory + File.separator + name,true);
		          //System.out.println("創建立目錄：" + outputDirectory + File.separator + name);
		        }else {
		          String fileName = zipEntry.getName();
		          fileName = fileName.replace('\\', '/');
		        
		          if (fileName.indexOf("/") != -1){
		              createDirectory(outputDirectory,
		                              fileName.substring(0, fileName.lastIndexOf("/")) ,log);
		              fileName=fileName.substring(fileName.lastIndexOf("/")+1,fileName.length());
		          }

		          File f = new File(outputDirectory + File.separator + zipEntry.getName());

		          f.createNewFile();
		          InputStream in = zipFile.getInputStream(zipEntry);
		          FileOutputStream out=new FileOutputStream(f);

		          byte[] by = new byte[1024];
		          int c;
		          while ( (c = in.read(by)) != -1) {
		            out.write(by, 0, c);
		          }
		          out.close();
		          in.close();
		        }
		      }
		    }catch (Exception ex) {
		      log.append(ex.getMessage(),true);
		      //System.out.println(ex.getMessage());
		    }
		        
		}

		
		/**
		 * 建立 zip 檔
		 * @param srcFile	想要壓縮的資料夾
		 * @param targetZip	壓縮zip檔
		 * @throws IOException
		 * @throws FileNotFoundException
		 */
		public void makeZip(File srcFile, File targetZip)throws IOException, FileNotFoundException{      
			makeZip(srcFile,  targetZip ,new LogWriter(null));			
		}
		
	    public void makeZip(File srcFile, File targetZip ,LogWriter log)throws IOException, FileNotFoundException{      
		  ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetZip));
		  String dir="";
	      recurseFiles(srcFile,zos,dir ,log);
	      zos.close();
	    }
		 
		   /**
		    * 壓縮 主程式
		    * @param file
		    * @param zos
		    * @throws IOException
		    * @throws FileNotFoundException
		    */
		   private void recurseFiles(File file, ZipOutputStream zos, String dir ,LogWriter log)
		      throws IOException, FileNotFoundException {
			   //目錄
		      if (file.isDirectory()) {
		    	  //log.append("找到資料夾:"+file.getName(),true);
		    	  //System.out.println("找到資料夾:"+file.getName());
		    	  dir += file.getName()+File.separator;
		         String[] fileNames = file.list();
		         if (fileNames != null) {        	 
		            for (int i=0; i < fileNames.length ; i++)  {            	
		               recurseFiles(new File(file, fileNames[i]), zos,dir ,log);
		            }
		         }
		      }else{
		    	  //log.append("壓縮檔案:"+file.getName(),true);
		    	  //System.out.println("壓縮檔案:"+file.getName());
		    	  
		         byte[] buf = new byte[1024];
		         int len;
		 
		         //Create a new Zip entry with the file's name.
		         dir = dir.substring(dir.indexOf(File.separator)+1);
		         ZipEntry zipEntry = new ZipEntry(dir+file.getName());
		         //Create a buffered input stream out of the file
		         //we're trying to add into the Zip archive.
		         FileInputStream fin = new FileInputStream(file);
		         BufferedInputStream in = new BufferedInputStream(fin);
		         zos.putNextEntry(zipEntry);
		         //Read bytes from the file and write into the Zip archive.
		 
		         while ((len = in.read(buf)) >= 0) {
		            zos.write(buf, 0, len);
		         }
		 
		         //Close the input stream.
		         in.close();
		 
		         //Close this entry in the Zip stream.
		         zos.closeEntry();
		      }
		   }
}
