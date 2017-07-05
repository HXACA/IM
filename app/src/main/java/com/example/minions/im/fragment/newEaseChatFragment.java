package com.example.minions.im.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.SharedElementCallback;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
*/
public class newEaseChatFragment extends EaseChatFragment {

    @Override
    public void showTitleBar() {
        super.showTitleBar();
    }

    @Override
    public void hideTitleBar() {
        super.hideTitleBar();
    }

    @Override
    protected void hideSoftKeyboard() {
        super.hideSoftKeyboard();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void setUpView() {
        super.setUpView();
    }

    @Override
    protected void registerExtendMenuItem() {
        super.registerExtendMenuItem();
    }

    @Override
    protected void onConversationInit() {
        super.onConversationInit();
    }

    @Override
    protected void onMessageListInit() {
        super.onMessageListInit();
    }

    @Override
    protected void setListItemClickListener() {
        super.setListItemClickListener();
    }

    @Override
    protected void setRefreshLayoutListener() {
        super.setRefreshLayoutListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onChatRoomViewCreation() {
        super.onChatRoomViewCreation();
    }

    @Override
    protected void addChatRoomChangeListenr() {
        super.addChatRoomChangeListenr();
    }

    @Override
    protected void showChatroomToast(String toastContent) {
        super.showChatroomToast(toastContent);
    }

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        super.onMessageReceived(messages);
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        super.onCmdMessageReceived(messages);
    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> messages) {
        super.onMessageReadAckReceived(messages);
    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> messages) {
        super.onMessageDeliveryAckReceived(messages);
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object change) {
        super.onMessageChanged(emMessage, change);
    }

    @Override
    protected void inputAtUsername(String username, boolean autoAddAtSymbol) {
        super.inputAtUsername(username, autoAddAtSymbol);
    }

    @Override
    protected void inputAtUsername(String username) {
        super.inputAtUsername(username);
    }

    @Override
    protected void sendTextMessage(String content) {
        super.sendTextMessage(content);
    }

    @Override
    protected void sendBigExpressionMessage(String name, String identityCode) {
        super.sendBigExpressionMessage(name, identityCode);
    }

    @Override
    protected void sendVoiceMessage(String filePath, int length) {
        super.sendVoiceMessage(filePath, length);
    }

    @Override
    protected void sendImageMessage(String imagePath) {
        super.sendImageMessage(imagePath);
    }

    @Override
    protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        super.sendLocationMessage(latitude, longitude, locationAddress);
    }

    @Override
    protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
        super.sendVideoMessage(videoPath, thumbPath, videoLength);
    }

    @Override
    protected void sendFileMessage(String filePath) {
        super.sendFileMessage(filePath);
    }

    @Override
    protected void sendMessage(EMMessage message) {
        super.sendMessage(message);
    }

    @Override
    public void resendMessage(EMMessage message) {
        super.resendMessage(message);
    }

    @Override
    protected void selectFileFromLocal() {
        super.selectFileFromLocal();
    }

    @Override
    protected void sendPicByUri(Uri selectedImage) {
        super.sendPicByUri(selectedImage);
    }

    @Override
    protected void sendFileByUri(Uri uri) {
        super.sendFileByUri(uri);
    }

    @Override
    protected void selectPicFromCamera() {
        super.selectPicFromCamera();
    }

    @Override
    protected void selectPicFromLocal() {
        super.selectPicFromLocal();
    }

    @Override
    protected void emptyHistory() {
        super.emptyHistory();
    }

    @Override
    protected void toGroupDetails() {
        super.toGroupDetails();
    }

    @Override
    protected void hideKeyboard() {
        super.hideKeyboard();
    }

    @Override
    protected void forwardMessage(String forward_msg_id) {
        super.forwardMessage(forward_msg_id);
    }

    @Override
    public void setChatFragmentListener(EaseChatFragmentHelper chatFragmentHelper) {
        super.setChatFragmentListener(chatFragmentHelper);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void setInitialSavedState(SavedState state) {
        super.setInitialSavedState(state);
    }

    @Override
    public void setTargetFragment(Fragment fragment, int requestCode) {
        super.setTargetFragment(fragment, requestCode);
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(retain);
    }

    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

    @Override
    public LoaderManager getLoaderManager() {
        return super.getLoaderManager();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        super.startActivity(intent, options);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return super.shouldShowRequestPermissionRationale(permission);
    }

    @Override
    public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        return super.getLayoutInflater(savedInstanceState);
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void registerForContextMenu(View view) {
        super.registerForContextMenu(view);
    }

    @Override
    public void unregisterForContextMenu(View view) {
        super.unregisterForContextMenu(view);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }

    @Override
    public void setEnterSharedElementCallback(SharedElementCallback callback) {
        super.setEnterSharedElementCallback(callback);
    }

    @Override
    public void setExitSharedElementCallback(SharedElementCallback callback) {
        super.setExitSharedElementCallback(callback);
    }

    @Override
    public void setEnterTransition(Object transition) {
        super.setEnterTransition(transition);
    }

    @Override
    public Object getEnterTransition() {
        return super.getEnterTransition();
    }

    @Override
    public void setReturnTransition(Object transition) {
        super.setReturnTransition(transition);
    }

    @Override
    public Object getReturnTransition() {
        return super.getReturnTransition();
    }

    @Override
    public void setExitTransition(Object transition) {
        super.setExitTransition(transition);
    }

    @Override
    public Object getExitTransition() {
        return super.getExitTransition();
    }

    @Override
    public void setReenterTransition(Object transition) {
        super.setReenterTransition(transition);
    }

    @Override
    public Object getReenterTransition() {
        return super.getReenterTransition();
    }

    @Override
    public void setSharedElementEnterTransition(Object transition) {
        super.setSharedElementEnterTransition(transition);
    }

    @Override
    public Object getSharedElementEnterTransition() {
        return super.getSharedElementEnterTransition();
    }

    @Override
    public void setSharedElementReturnTransition(Object transition) {
        super.setSharedElementReturnTransition(transition);
    }

    @Override
    public Object getSharedElementReturnTransition() {
        return super.getSharedElementReturnTransition();
    }

    @Override
    public void setAllowEnterTransitionOverlap(boolean allow) {
        super.setAllowEnterTransitionOverlap(allow);
    }

    @Override
    public boolean getAllowEnterTransitionOverlap() {
        return super.getAllowEnterTransitionOverlap();
    }

    @Override
    public void setAllowReturnTransitionOverlap(boolean allow) {
        super.setAllowReturnTransitionOverlap(allow);
    }

    @Override
    public boolean getAllowReturnTransitionOverlap() {
        return super.getAllowReturnTransitionOverlap();
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
