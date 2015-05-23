package lucene;

import java.io.IOException;


public class FenciMain {  
    public static void main(String[] args) throws IOException {
        PaoDingFenci pd = new PaoDingFenci();
        String text = "压铸件的致密性是评价其质量的重要指标,通常用密度来衡量。以AM50压铸镁合金为研究"
        		+ "对象,采用多因素分析法设计了阶梯块试样和压铸工艺参数。通过压铸试验和压铸"
        		+ "件的密度测定,分析了压铸工艺参数对铸件密度的影响规律,获得致密度高的压铸件工艺参数为:"
        		+ "浇注温度650℃、模具温度180℃、铸造压力44MPa、低速速度0.25m/s、高速速度3m/s、高速位置230mm、增压"
        		+ "位置280mm、增压时间20ms、料饼厚度30mm。研究还表明,不同厚度阶梯块理想的压铸工艺参数不同。";
        System.out.println(pd.fenci01(text));
        
    }
}