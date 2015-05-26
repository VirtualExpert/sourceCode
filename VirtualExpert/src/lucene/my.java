package lucene;

import java.io.IOException;  
import java.io.StringReader;   
import org.apache.lucene.analysis.TokenStream;  
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;  
import org.wltea.analyzer.lucene.IKAnalyzer;  
  
public class my {  
    public static void main(String[] args) throws IOException {
        String text="��Al2(SO4)3��CO(NH2)2Ϊԭ��,ͨ�����ȳ����Ʊ���ǰ����Al(OH)3,�����յõ���ϸ��-Al2O3���塣�о�CO(NH2)2��Al2(SO4)3��Ħ���ȡ�Al2(SO4)3��ʼĦ��Ũ�ȡ���Ӧ�¶ȡ���Ӧʱ��ȶ�ǰ�����Ʊ���Ӱ�졣����X���������ǡ�ɨ��羵������/��ʾɨ�跨(DTA/TGA)�ȶ����ִ�������⼼���Է�������ܽ����˱������������:��CO(NH2)2��Al2(SO4)3��Ħ����Ϊ10:1��Al2(SO4)3��ʼŨ��Ϊ0.05mol/L����Ӧ�¶�Ϊ90�桢��Ӧʱ��Ϊ60min��������,�ܵõ���ɢ�������á�����Ϊ2��m���������ȷֲ����ȵ����γ�ϸ��-Al2O3���塣";  
        //�����ִʶ���  
        IKAnalyzer anal=new IKAnalyzer(true);       
        StringReader reader=new StringReader(text);  
        //�ִ�  
        TokenStream ts=anal.tokenStream("", reader);  
        CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);  
        //�����ִ�����  
        while(ts.incrementToken()){  
        	String temp=term.toString();
        	System.out.println(temp);
           // System.out.print(term.toString()+"|");
        }
        reader.close();  
        System.out.println();  
    }  
  
}  