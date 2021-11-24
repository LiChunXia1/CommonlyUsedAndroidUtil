import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class EjectView implements View.OnTouchListener {

    public static WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
    public static WindowManager windowManager;
    public static View view;

    // 初始化
    public static void initialization(Activity activity, View views , int sX, int sY, int sWidth, int sHeight)  {
        // 获得视图
        view = views;
        
        // 获得系统服务 全局窗口
        windowManager = (WindowManager)activity.getSystemService(Context.WINDOW_SERVICE);

        // 获得像素
        layoutParams.format = PixelFormat.RGBA_8888;
        
        // 设置布局参数
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        
        // View 设置屏幕全局视图焦点
        layoutParams.flags = view.findFocus().ACCESSIBILITY_LIVE_REGION_NONE;
        // View 弹出的 宽度
        layoutParams.width = sWidth;
        // View 弹出的 高度
        layoutParams.height = sHeight;
        // View 弹出的 X坐标
        layoutParams.x = sX;
        // View 弹出的 Y坐标
        layoutParams.y = sY;

        // 判断SDK版本权限
        if (Build.VERSION.SDK_INT >= 24) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // android8.0用
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                // android7.0不能用TYPE_TOAST
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
        } else {
            // 以下代码块使得android6.0之后的用户不必再去手动开启悬浮窗权限
            String packname = activity.getPackageName();
            PackageManager pm = activity.getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", packname));
            if (permission) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            }
        }
        
        
    }

    // 删除View 
    public static void deleteView() {
        windowManager.removeViewImmediate(view);
    }
    
    // 弹出View
    public static void showView() {
        windowManager.addView(view, layoutParams);
    }
    
    // 设置为可移动窗口
    public static void movableWindow() {
        view.setOnTouchListener(new EjectView());
    }
    

    private int x;
    private int y;
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x = (int) event.getRawX();
                y = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int nowX = (int) event.getRawX();
                int nowY = (int) event.getRawY();
                int movedX = nowX - x;
                int movedY = nowY - y;
                x = nowX;
                y = nowY;
                layoutParams.x = layoutParams.x + movedX;
                layoutParams.y = layoutParams.y + movedY;

                // 更新悬浮窗控件布局
                windowManager.updateViewLayout(view, layoutParams);
                break;
            default:
                break;
        }
        return false;
    }
}
