package EntityConstruct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadFile {
	public StringBuilder stringContainer;
	public StringBuilder getString(){
		stringContainer=new StringBuilder();
		File file=new File("C:/Users/Administrator/Desktop/virtualExpert/lab/Èí¼þ±­/dataResult/ÎÄÏ×2.txt");		
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");
			BufferedReader reader = new BufferedReader(read);
			char[] buf = new char[10];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				stringContainer.append(readData);
				buf = new char[1024];
			}
			read.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stringContainer;
		
		
	}
}
