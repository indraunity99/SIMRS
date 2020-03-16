package com.mobile.rsupsanglah.simrsmobile.activity.ui;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.rsupsanglah.simrsmobile.R;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SharedPreference;
import com.mobile.rsupsanglah.simrsmobile.activity.control.SimrsmConstant;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    private WebView webView;
    ProgressBar bar;
    TextView txt, txtuserName;
    String KEY_CON = "", ipAddress = "";
    public static final String mypreference = "installpref";
    public static String ipfromserver = "ipKey";
    SharedPreferences sharedpreferences;
    String ipFromServer = "", namaUser = "";
    private static final int REQUEST_CODE = 1234;
    private final String googleDocs = "https://docs.google.com/viewer?url=";
    private static String ipnie = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Log.e("MAIN ACT", "");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        String cek_user = SharedPreference.getInstance(getApplicationContext()).getAppUser();

        bar = findViewById(R.id.loading);
        txt = findViewById(R.id.txtWait);

        webView = (WebView) findViewById(R.id.webView);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        ipFromServer = sharedpreferences.getString(ipfromserver, "");
        namaUser = sharedpreferences.getString("namaUser", "SIMARS MOBILE");

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.userNameTxt);
        navUsername.setText(namaUser);

        WebSettings webSettings = webView.getSettings();
        if (18 < Build.VERSION.SDK_INT) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
        chkStatus();
        //aktifkan javascript
        webSettings.setJavaScriptEnabled(true);

        //tampilkan zoom kontrol
        webSettings.setBuiltInZoomControls(true);

        // memberitahukan browser untuk mengaktifkan Wide ViewPort
        webSettings.setUseWideViewPort(true);

        // otomatis website akan diload dengan zoom out
        webSettings.setLoadWithOverviewMode(true);

        // otomatis menampilkan javascript window
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        //handling clicks
        webView.setWebViewClient(new MainActivity.myWebclient());
        String ip3 = this.getAndroidIP();
        Log.d("IP Device check  ----> ", ip3);

        webView.loadUrl(ipAddress + "/simrsm/index.php?app=new2");
        Log.d("URL ---------> ", webView.getUrl());
        //showing default fragment
        //displaySelectedFragment(R.id.nav_one);

         Log.d("User access", SharedPreference.getInstance(getApplicationContext()).getArrayList("idpegawai").toString());

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDescription,
                                        String mimetype, long contentLength) {
                /*
                    DownloadManager.Request
                        This class contains all the information necessary to request a new download.
                        The URI is the only required parameter. Note that the default download
                        destination is a shared volume where the system might delete your file
                        if it needs to reclaim space for system use. If this is a problem,
                        use a location on external storage (see setDestinationUri(Uri).
                */
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                /*
                    void allowScanningByMediaScanner ()
                        If the file to be downloaded is to be scanned by MediaScanner, this method
                        should be called before enqueue(Request) is called.
                */
                request.allowScanningByMediaScanner();

                /*
                    DownloadManager.Request setNotificationVisibility (int visibility)
                        Control whether a system notification is posted by the download manager
                        while this download is running or when it is completed. If enabled, the
                        download manager posts notifications about downloads through the system
                        NotificationManager. By default, a notification is shown only
                        when the download is in progress.

                        It can take the following values: VISIBILITY_HIDDEN, VISIBILITY_VISIBLE,
                        VISIBILITY_VISIBLE_NOTIFY_COMPLETED.

                        If set to VISIBILITY_HIDDEN, this requires the permission
                        android.permission.DOWNLOAD_WITHOUT_NOTIFICATION.

                    Parameters
                        visibility int : the visibility setting value
                    Returns
                        DownloadManager.Request this object
                */
                request.setNotificationVisibility(
                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                /*
                    DownloadManager
                        The download manager is a system service that handles long-running HTTP
                        downloads. Clients may request that a URI be downloaded to a particular
                        destination file. The download manager will conduct the download in the
                        background, taking care of HTTP interactions and retrying downloads
                        after failures or across connectivity changes and system reboots.
                */

                /*
                    String guessFileName (String url, String contentDisposition, String mimeType)
                        Guesses canonical filename that a download would have, using the URL
                        and contentDisposition. File extension, if not defined,
                        is added based on the mimetype

                    Parameters
                        url String : Url to the content
                        contentDisposition String : Content-Disposition HTTP header or null
                        mimeType String : Mime-type of the content or null

                    Returns
                        String : suggested filename
                */
                String fileName = URLUtil.guessFileName(url,contentDescription,mimetype);

                /*
                    DownloadManager.Request setDestinationInExternalPublicDir
                    (String dirType, String subPath)

                        Set the local destination for the downloaded file to a path within
                        the public external storage directory (as returned by
                        getExternalStoragePublicDirectory(String)).

                        The downloaded file is not scanned by MediaScanner. But it can be made
                        scannable by calling allowScanningByMediaScanner().

                    Parameters
                        dirType String : the directory type to pass to
                                         getExternalStoragePublicDirectory(String)
                        subPath String : the path within the external directory, including
                                         the destination filename

                    Returns
                        DownloadManager.Request this object

                    Throws
                        IllegalStateException : If the external storage directory cannot be
                                                found or created.
                */
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);

                DownloadManager dManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                /*
                    long enqueue (DownloadManager.Request request)
                        Enqueue a new download. The download will start automatically once the
                        download manager is ready to execute it and connectivity is available.

                    Parameters
                        request DownloadManager.Request : the parameters specifying this download

                    Returns
                        long : an ID for the download, unique across the system. This ID is used
                               to make future calls related to this download.
                */
                dManager.enqueue(request);
            }
        });



        if (SharedPreference.getInstance(getApplicationContext()).getArrayList("idpegawai").contains(cek_user)){
            navigationView.getMenu().findItem(R.id.nav_one).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_two).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_three).setVisible(true);/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            navigationView.setNavigationItemSelectedListener(this);
        } else {
            navigationView.getMenu().findItem(R.id.nav_one).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_two).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_three).setVisible(false);
            navigationView.setNavigationItemSelectedListener(this);
        }

    }


    void chkStatus() {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        final String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnected()) {
            KEY_CON = "Wifi";
            toastMessage("Wifi Connected");
            if (ip.contains("10.20.")) {
                ipAddress = SimrsmConstant.ServiceType.LOCALIP;
            } else {
                ipAddress = "http://" + ipFromServer + ":9024";
            }
        } else if (mobile.isConnected()) {
            KEY_CON = "Mobile";
            toastMessage("Mobile Internet Connected");
            ipAddress = "http://" + ipFromServer + ":9024";
        } else {
            toastMessage("No Internet Connection");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history

        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private String getAndroidIP() {
        try {
            String interfaces = "";
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        interfaces = interfaces + inetAddress.getHostAddress();
                    }
                }
            }
            return (interfaces);
        } catch (SocketException ex) {
            Log.i("externalip", ex.toString());
        }
        return null;
    }

    public class myWebclient extends WebViewClient {
        boolean timeout;

        public myWebclient() {
            bar.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            txt.setVisibility(View.VISIBLE);
            timeout = true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);


            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (timeout) {
                        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
                        ipnie=  sharedpreferences.getString(ipfromserver, "");
                        //String _url = "http://203.142.76.66:2024"+"/simrsm/index.php?app=new";
                        ipAddress = "http://"+ipnie+":9024";

                        // webview.loadUrl(_url);
                    }
                }
            }).start();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            bar.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            txt.setVisibility(View.GONE);
            timeout = false;

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.endsWith(".pdf")){
                String pdfUrl = googleDocs + url;
                view.loadUrl(pdfUrl);
            } else {
                view.loadUrl(url);
            }

            return true;
        }


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemClose:
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
//            case R.id.itemLogout:
//                android.webkit.CookieManager cookieManager = CookieManager.getInstance();
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
//                        // a callback which is executed when the cookies have been removed
//                        @Override
//                        public void onReceiveValue(Boolean aBoolean) {
//                            Log.d("COOKIES", "Cookie removed: " + aBoolean);
//                        }
//                    });
//                }
//                else cookieManager.removeAllCookie();
//                webview.loadUrl(ipAddress+"/simrsm/index.php?app=new");
//                //Toast.makeText(this, "UNDERCONSTRUCTION", Toast.LENGTH_SHORT).show();
//                return true;
            case R.id.itemHelp:
                return true;
            case R.id.itemRefresh:
                finish();
                startActivity(getIntent());
                return true;
//            case R.id.itemQr:
//                Intent intentQr = new Intent(MainActivity.this, QRreaderActivity.class);
//                startActivity(intentQr);
//                //Toast.makeText(this, "UNDERCONSTRUCTION", Toast.LENGTH_SHORT).show();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        // item id is being passed into the method here
        displaySelectedFragment(item.getItemId());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setScreenTitle(int item_id) {
        String title = "";
        switch (item_id) {
            case R.id.nav_one:
                title = "SIMARS MOBILE";
                break;
            case R.id.nav_two:
                title = "SIMARS MOBILE";
                break;

            case R.id.nav_three:
                title = "SIMARS MOBILE";
                break;
        }

        getSupportActionBar().setTitle(title);
    }

    public void displaySelectedFragment(int item_id) {


        Fragment fragment = null;

        switch (item_id) {

            case R.id.nav_one:
//                toastMessage("Check 1");
                break;
            case R.id.nav_two:
                Intent intentJenis = new Intent(MainActivity.this, JenisPerbaikanActivity.class);
                startActivity(intentJenis);
                finish();
                break;
            case R.id.nav_three:
                Intent intentDashboard = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intentDashboard);
                finish();
                break;

        }
        if (fragment != null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //this is where the id of the FrameLayout is being mentioned. Hence the fragment would be loaded into the framelayout
            ft.replace(R.id.container, fragment);
            ft.commit();
        }

        /** setting title to the screen **/
        setScreenTitle(item_id);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
