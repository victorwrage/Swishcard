package com.qhw.swishcardapp.utils;

public class EmvCoreContants {
	
	public static final int TYPE_CASH            = 0x01;    //��������(�ֽ�)
	public static final int TYPE_GOODS           = 0x02;    //��������(����)
	public static final int TYPE_SERVICE         = 0x04;    //��������(����)
	public static final int TYPE_CASHBACK        = 0x08;    //��������(����)
	public static final int TYPE_INQUIRY         = 0x10;    //��������(��ѯ)
	public static final int TYPE_PAYMENT         = 0x20;    //��������(֧��)
	public static final int TYPE_ADMINISTRATIVE  = 0x40;    //��������(����)
	public static final int TYPE_TRANSFER        = 0x80;    //��������(ת��)
	
	public static final int PART_MATCH = 0x00;    //ASI(����ƥ��)
	public static final int FULL_MATCH = 0x01;    //ASI(��ȫƥ��)
	
	public static final int AC_AAC = 0x00; 
	public static final int AC_TC = 0x01;
	public static final int AC_ARQC = 0x80;
	
	public static final int ONLINE_APPROVE =   0x00;     //����������(������׼)     
	public static final int ONLINE_FAILED  =   0x01;     //����������(����ʧ��) 
	public static final int ONLINE_REFER   =   0x02;     //����������(�����ο�)
	public static final int ONLINE_DENIAL  =   0x03;     //����������(�����ܾ�)
	public static final int ONLINE_ABORT   =   0x04;     //����PBOC(������ֹ)
	
	
	public static final int EMV_OK            =  0;      //�ɹ�  
	public static final int ERR_EMVRSP        = (-1);      //���������
	public static final int ERR_APPBLOCK      = (-2);      //Ӧ������
	public static final int ERR_NOAPP         = (-3);      //��Ƭ��û��EMVӦ��
	public static final int ERR_USERCANCEL    = (-4);      //�û�ȡ����ǰ��������
	public static final int ERR_TIMEOUT       = (-5);      //�û�������ʱ
	public static final int ERR_EMVDATA       = (-6);      //��Ƭ���ݴ���
	public static final int ERR_NOTACCEPT     = (-7);      //���ײ�����
	public static final int ERR_EMVDENIAL     = (-8);      //���ױ��ܾ�
	public static final int ERR_KEYEXP        = (-9);      //��Կ����
	public static final int ERR_NOPINPAD      = (-10);     //û��������̻���̲�����
	public static final int ERR_NOPIN         = (-11);     //û��������û���������������
	public static final int ERR_CAPKCHECKSUM  = (-12);     //��֤������ԿУ��ʹ���
	public static final int ERR_NOTFOUND      = (-13);     //û���ҵ�ָ�������ݻ�Ԫ��
	public static final int ERR_NODATA        = (-14);     //ָ��������Ԫ��û������
	public static final int ERR_OVERFLOW      = (-15);     //�ڴ����
	public static final int ERR_NORECORD      = (-17);     //�޼�¼
	public static final int ERR_ICCRESET      = (-19);     //IC����λʧ��
	public static final int ERR_ICCCMD        = (-20);     //IC����ʧ��
	public static final int ERR_ICCBLOCK      = (-21);     //IC������ 
	public static final int ERR_ICCNORECORD   = (-22);     //IC���޼�¼
	public static final int ERR_GPORSP        = (-27);     //err from GPO
	public static final int ERR_LASTREAD      = (-29);    //�����һ����¼ʧ��.
	public static final int ERR_PINBLOCK      = (-38);     //PIN����
	public static final int ERR_APPSEL        = (-40);     //Ӧ��ѡ�����
	public static final int	ERR_JNIPARAM	    = (-41); //JNI��������

}
