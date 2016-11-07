package com.liaofan_01.kindmanage;

import com.amap.api.maps2d.model.LatLng;

public class Constants {
	public static final String CACHE_DIR = "/liaofan/";
	public static final String INPUT_METHOD_SERVICE = "input_method";

	public static final int ERROR = 1001;// �����쳣
	public static final int ROUTE_START_SEARCH = 2000;
	public static final int ROUTE_END_SEARCH = 2001;
	public static final int ROUTE_BUS_RESULT = 2002;// ·���滮�й���ģʽ
	public static final int ROUTE_DRIVING_RESULT = 2003;// ·���滮�мݳ�ģʽ
	public static final int ROUTE_WALK_RESULT = 2004;// ·���滮�в���ģʽ
	public static final int ROUTE_NO_RESULT = 2005;// ·���滮û�����������

	public static final int GEOCODER_RESULT = 3000;// ������������������ɹ�
	public static final int GEOCODER_NO_RESULT = 3001;// ������������������û������

	public static final int POISEARCH = 4000;// poi���������
	public static final int POISEARCH_NO_RESULT = 4001;// poiû�����������
	public static final int POISEARCH_NEXT = 5000;// poi������һҳ

	public static final int BUSLINE_LINE_RESULT = 6001;// ������·��ѯ
	public static final int BUSLINE_id_RESULT = 6002;// ����id��ѯ
	public static final int BUSLINE_NO_RESULT = 6003;// �쳣���

	public static final LatLng BEIJING = new LatLng(39.90403, 116.407525);// �����о�γ��
	public static final LatLng ZHONGGUANCUN = new LatLng(39.983456, 116.3154950);// �������йش徭γ��
	public static final LatLng SHANGHAI = new LatLng(31.238068, 121.501654);// �Ϻ��о�γ��
	public static final LatLng FANGHENG = new LatLng(39.989614, 116.481763);// ����������ľ�γ��
	public static final LatLng CHENGDU = new LatLng(30.679879, 104.064855);// �ɶ��о�γ��
	public static final LatLng XIAN = new LatLng(34.341568, 108.940174);// �����о�γ��
	public static final LatLng ZHENGZHOU = new LatLng(34.7466, 113.625367);// ֣���о�γ��

	public static final int TAKE_PICTURE = 2;
	public static final int RESULT_LOAD_IMAGE = 3;
	public static final int CUT_PHOTO_REQUEST_CODE = 4;
	public static final int SELECTIMG_SEARCH = 5;

	/** Standard activity result: operation succeeded. */
	public static final int RESULT_OK = -1;
}
