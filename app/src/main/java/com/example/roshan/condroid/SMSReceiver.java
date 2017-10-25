package com.example.roshan.condroid;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class SMSReceiver extends BroadcastReceiver {

    private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private String password, contact1, contact2, contact3, contact4;

    @Override
    public void onReceive(Context context, Intent intent) {

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (intent.getAction().equals(ACTION_SMS_RECEIVED)) {
            if (intent.getExtras() != null) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("Credentials", Context.MODE_PRIVATE);

                Object[] pdus = (Object[]) intent.getExtras().get("pdus");
                for (int i = 0; i < pdus.length; i++) {
                    SmsMessage message;
                    if (Build.VERSION.SDK_INT < 23) {
                        message = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    } else {
                        message = SmsMessage.createFromPdu((byte[]) pdus[i], intent.getExtras().getString("format"));
                    }

                    String msgSender = message.getOriginatingAddress();
                    String msgBody = message.getMessageBody();

                    password = sharedPreferences.getString("Password", null);
                    contact1 = sharedPreferences.getString("Contact1", null);
                    contact2 = sharedPreferences.getString("Contact2", null);
                    contact3 = sharedPreferences.getString("Contact3", null);
                    contact4 = sharedPreferences.getString("Contact4", null);

                    if (msgSender.contains(contact1) | msgSender.contains(contact2) | msgSender.contains(contact3) | msgSender.contains(contact4)) {
                        String[] splitArray = msgBody.split("#", 2);
                        if (splitArray.length == 2) {
                            if (splitArray[0].trim().equals(password)) {
                                msgBody = splitArray[1];
                                msgBody = msgBody.trim();

                                Toast.makeText(context, "Message Received from trusted contacts", Toast.LENGTH_LONG).show();
                                if (Pattern.matches("ring", msgBody)) {
                                    final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

                                    final int savedRingerMode = audioManager.getRingerMode();
                                    final int savedRingerVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);

                                    audioManager.setStreamVolume(AudioManager.RINGER_MODE_NORMAL, audioManager.getStreamMaxVolume(AudioManager.RINGER_MODE_NORMAL), 0);
                                    final Ringtone ringtone = RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

                                    long[] pattern = {0, 2000, 1000};
                                    ringtone.play();
                                    vibrator.vibrate(pattern, 0);

                                    new CountDownTimer(30000, 2000) {
                                        @Override
                                        public void onTick(long milisUntilFinished) {
                                            if (!ringtone.isPlaying()) {
                                                ringtone.play();
                                            }
                                        }

                                        @Override
                                        public void onFinish() {
                                            ringtone.stop();
                                            audioManager.setStreamVolume(AudioManager.STREAM_RING, savedRingerVolume, 0);
                                            audioManager.setRingerMode(savedRingerMode);
                                            vibrator.cancel();
                                        }
                                    }.start();
                                } else if (Pattern.matches("turn\\son\\swifi", msgBody)) {
                                    wifiManager.setWifiEnabled(true);
                                } else if (Pattern.matches("turn\\soff\\swifi", msgBody)) {
                                    wifiManager.setWifiEnabled(false);
                                } else if (Pattern.matches("silent\\smode", msgBody)) {
                                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                                } else if (Pattern.matches("vibrate\\smode", msgBody)) {
                                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                                } else if (Pattern.matches("general\\smode", msgBody)) {
                                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                } else if (Pattern.matches("turn\\son\\sdata", msgBody)) {
                                    try {

                                        final Class conmanClass = Class.forName(connectivityManager.getClass().getName());
                                        final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
                                        iConnectivityManagerField.setAccessible(true);
                                        final Object iConnectivityManager = iConnectivityManagerField.get(connectivityManager);
                                        final Class iConnectivityManagerClass = Class.forName(
                                                iConnectivityManager.getClass().getName());
                                        final Method setMobileDataEnabledMethod = iConnectivityManagerClass
                                                .getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
                                        setMobileDataEnabledMethod.setAccessible(true);

                                        setMobileDataEnabledMethod.invoke(iConnectivityManager, true);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (Pattern.matches("turn\\son\\sflash", msgBody)) {
                                    try {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                                            String cameraId = cameraManager.getCameraIdList()[0];
                                            cameraManager.setTorchMode(cameraId, true);
                                        } else {
                                            Camera camera;
                                            Camera.Parameters parameters;
                                            boolean hasFlash = context.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                                            if (hasFlash) {
                                                camera = Camera.open();
                                                parameters = camera.getParameters();
                                                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                                                camera.setParameters(parameters);
                                                camera.startPreview();
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else if (Pattern.matches("turn\\soff\\sflash", msgBody)) {
                                    try {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                                            String cameraId = cameraManager.getCameraIdList()[0];
                                            cameraManager.setTorchMode(cameraId, false);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (Pattern.matches("call\\s[0-9]{10}", msgBody)) {
                                    String[] splitArray2 = msgBody.split("\\s", 2);
                                    msgBody = splitArray2[1];
                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:" + msgBody));
                                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        return;
                                    }
                                    context.startActivity(callIntent);

                                } else if (Pattern.matches("turn\\son\\sbluetooth", msgBody)) {
                                    bluetoothAdapter.enable();
                                } else if (Pattern.matches("turn\\soff\\sbluetooth", msgBody)) {
                                    bluetoothAdapter.disable();
                                }
                            }
                        }
                    }

                }

            }

        }
    }


}
