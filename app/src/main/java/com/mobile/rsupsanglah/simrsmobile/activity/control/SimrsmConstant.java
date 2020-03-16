package com.mobile.rsupsanglah.simrsmobile.activity.control;

public class SimrsmConstant {

        public class ServiceCode {
            public static final int HOME = 1;
        }
        // web service url constants
        public class ServiceType {
//        public static final String URLDEVICE = "/preserv/index.php?func=devsave&pid=";
//        public static final String URLPIN = "/preserv/index.php?func=devget&pin=";

            public static final String LOCALIP ="http://10.20.2.14";
            public static final String URLGETIP ="http://sanglahhospitalbali.com/service/ip.php";
            public static final String URLDEVICE = "/simserv/index.php?fun=devtoken&dev=";
            public static final String URLPIN = "/simserv/index.php?fun=devget&pin=";
            public static final String URLGETNAME = "/simserv/index.php?fun=getpegawai&pid=";
            public static final String URLCHECKDEVICE = "/simserv/index.php?fun=getserial&serial=";
            public static final String URLCHECKVERSION =  "/simserv/index.php?fun=ver&ver=";
            public static final String URLMARKET =  "market://details?id=";
            public static final String URLPLAYSTORE = "https://play.google.com/store/apps/details?id=";

            public static final String URLLISTJENISPERBAIKAN = "/simserv/index.php?fun=order_menu";
            public static final String URLJENISORDER = "/simserv/index.php?fun=sarana&jns=";
            public static final String URLINPUTTEXT = "/simserv/index.php?fun=saveorder&appid=";
            public static final String URLRESPONKELUHAN = "/simserv/index.php?fun=listrespon";
            public static final String URLRESPONKELUHANVIEW = "/simserv/index.php?fun=getorder&order_id=";

            public static final String URLSAVELISTBAHAN = "/simserv/index.php?fun=save_bahan&order_id=";
            public static final String URLGETLISTBAHAN = "/simserv/index.php?fun=bahan&keyword=";
            public static final String URLGETNAMAPELAKSANA = "/simserv/index.php?fun=person&keyword=";
            public static final String URLSAVEHISTORYPEKERJAAN = "/simserv/index.php?fun=respon&order_id=";
            public static final String URLGETHISTORYPEKERJAAN = "/simserv/index.php?fun=get_respon&order_id=";
            public static final String URLGETHISTORYBAHAN = "/simserv/index.php?fun=get_bahan&order_id=";
            public static final String URLDELETEHISTORYPEKERJAAN = "/simserv/index.php?fun=delete&table=respon&id=";
            public static final String URLDELETEHISTORYBAHAN = "/simserv/index.php?fun=delete&table=bahan&id=";
            public static final String URLFINISHRESPON= "/simserv/index.php?fun=finish&id=";

            public static final String URLAKSES = "/simserv/index.php?fun=akses";
            public static final String URLSAVEIMAGE = "/simserv/imageupload.php";
            public static final String URLGETIMAGES = "/simserv/image_up.php?id_respon=";

            public static final String URLGETDATANUMBERDASHBOARD = "/simserv/index.php?fun=resume&user=";
            public static final String URLDIKERJAKAN = "/simserv/index.php?fun=summary&respon=dikerjakan&user=";
            public static final String URLPENDING = "/simserv/index.php?fun=summary&respon=pending&user=";
            public static final String URLSELESAI = "/simserv/index.php?fun=summary&respon=selesai&user=";

        }
        // webservice key constants
        public class Params {
            public static final String PID = "pegawaiID";
            public static final String DID = "deviceID";
            public static final String BRN = "brand";
            public static final String MDL = "model";
            public static final String SER = "serial";
            public static final String PIN = "pinabsen";
            public static final String NIP = "nip";
        }
    }
