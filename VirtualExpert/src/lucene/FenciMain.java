package lucene;

import java.io.IOException;


public class FenciMain {  
    public static void main(String[] args) throws IOException {
        PaoDingFenci pd = new PaoDingFenci();
        String text = "ѹ����������������������������Ҫָ��,ͨ�����ܶ�����������AM50ѹ��þ�Ͻ�Ϊ�о�"
        		+ "����,���ö����ط���������˽��ݿ�������ѹ�����ղ�����ͨ��ѹ�������ѹ��"
        		+ "�����ܶȲⶨ,������ѹ�����ղ����������ܶȵ�Ӱ�����,������ܶȸߵ�ѹ�������ղ���Ϊ:"
        		+ "��ע�¶�650�桢ģ���¶�180�桢����ѹ��44MPa�������ٶ�0.25m/s�������ٶ�3m/s������λ��230mm����ѹ"
        		+ "λ��280mm����ѹʱ��20ms���ϱ����30mm���о�������,��ͬ��Ƚ��ݿ������ѹ�����ղ�����ͬ��";
        System.out.println(pd.fenci01(text));
        
    }
}