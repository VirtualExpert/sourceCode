package lucene;

import java.io.IOException;  
import java.io.StringReader;   
import org.apache.lucene.analysis.TokenStream;  
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;  
import org.wltea.analyzer.lucene.IKAnalyzer;  
  
public class my {  
    public static void main(String[] args) throws IOException {
        String text="以Al2(SO4)3和CO(NH2)2为原料,通过均匀沉淀法制备出前驱物Al(OH)3,并煅烧得到超细α-Al2O3粉体。研究CO(NH2)2和Al2(SO4)3的摩尔比、Al2(SO4)3起始摩尔浓度、反应温度、反应时间等对前驱物制备的影响。利用X射线衍射仪、扫描电镜、热重/差示扫描法(DTA/TGA)等多种现代分析检测技术对粉体的性能进行了表征。结果表明:在CO(NH2)2和Al2(SO4)3的摩尔比为10:1、Al2(SO4)3起始浓度为0.05mol/L、反应温度为90℃、反应时间为60min的条件下,能得到分散性能良好、粒径为2μm左右且粒度分布均匀的球形超细α-Al2O3粉体。";  
        //创建分词对象  
        IKAnalyzer anal=new IKAnalyzer(true);       
        StringReader reader=new StringReader(text);  
        //分词  
        TokenStream ts=anal.tokenStream("", reader);  
        CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);  
        //遍历分词数据  
        while(ts.incrementToken()){  
        	String temp=term.toString();
        	System.out.println(temp);
           // System.out.print(term.toString()+"|");
        }
        reader.close();  
        System.out.println();  
    }  
  
}  