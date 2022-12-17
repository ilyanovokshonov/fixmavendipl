package org.example;
   
import java.io.File;
import java.io.Serializable;
import java.util.Scanner;
public class CreateDirectory implements Serializable {
    public static void folder(){ 
	 
	  String chaindata_dir="C:\\chaindata"; 
      File D = new File(chaindata_dir);    
      boolean D1 = D.mkdir();  
      if(D1){   
      }else{  

      }  
   }  
}