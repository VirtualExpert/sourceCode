package lucene;
import java.io.IOException;  
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;  
import org.apache.lucene.analysis.Token;  
import org.apache.lucene.analysis.TokenStream;  
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

import net.paoding.analysis.analyzer.PaodingAnalyzer;
import net.paoding.analysis.knife.Knife;
import net.paoding.analysis.knife.Paoding;





public class PaoDingFenci {
	
	
    Analyzer analyzer = new PaodingAnalyzer();
    
    

    public PaoDingFenci() {
        //
    }

    public String fenci01(String text) throws IOException {   	
        StringBuffer sb = new StringBuffer();
        StringReader reader = new StringReader(text);
        TokenStream ts = this.analyzer.tokenStream(text, reader);

        TermAttribute termAtt = (TermAttribute) ts
                .addAttribute(TermAttribute.class);
        while (ts.incrementToken()) {
            sb.append(termAtt.term());
            sb.append(" ");
        }
        return sb.toString();
    }

//	public String fenci02(String text) throws IOException {
//        StringBuffer sb = new StringBuffer();
//        StringReader reader = new StringReader(text);
//        TokenStream ts = this.analyzer.tokenStream(text, reader);
//
//        Token t;
//        t = ts.next();
//        while (t != null) {
//            sb.append(t.termText());
//            sb.append(" ");
//            t = ts.next();
//        }
//        return sb.toString();
//    }
}
