package com.example.fcronin.activities;

import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.fcronin.Adapters.MessageAdapter;
import com.example.fcronin.Interfaces.OnMessageItemClick;
import com.example.fcronin.MainActivity;
import com.example.fcronin.Models.Chat;
import com.example.fcronin.Models.Contact;
import com.example.fcronin.Models.DownloadFileEvent;
import com.example.fcronin.Models.Group;
import com.example.fcronin.Models.Message;
import com.example.fcronin.Models.Status;
import com.example.fcronin.Models.User;
import com.example.fcronin.R;
import com.example.fcronin.viewHolders.MessageAttachmentRecordingViewHolder;
import com.kbeanie.multipicker.api.AudioPicker;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.FilePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.VideoPicker;
import com.kbeanie.multipicker.api.callbacks.AudioPickerCallback;
import com.kbeanie.multipicker.api.callbacks.FilePickerCallback;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.callbacks.VideoPickerCallback;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends BaseActivity implements OnMessageItemClick,
        MessageAttachmentRecordingViewHolder.RecordingViewInteractor, View.OnClickListener, ImagePickerCallback,
        FilePickerCallback, AudioPickerCallback, VideoPickerCallback {
    private static final int REQUEST_CODE_CONTACT = 1;
    private static final int REQUEST_PLACE_PICKER = 2;
    private static final int REQUEST_CODE_PLAY_SERVICES = 3;
    private static final int REQUEST_CODE_UPDATE_USER = 753;
    private static final int REQUEST_CODE_UPDATE_GROUP = 357;
    private static final int REQUEST_PERMISSION_RECORD = 159;
    private static final int REQUEST_PERMISSION_CALL = 951;
    private static final String EXTRA_DATA_GROUP1 = "EXTRA_DATA_GROUP1";
    private static String EXTRA_DATA_GROUP = "extradatagroup";
    private static String EXTRA_DATA_USER = "extradatauser";
    private static String EXTRA_DATA_LIST = "extradatalist";
    private static String DELETE_TAG = "deletetag";
    private MessageAdapter messageAdapter;
    private ArrayList<Message> dataList = new ArrayList<>();
    private RealmResults<Chat> queryResult;
    private String chatChild, userOrGroupId;
    private int countSelected = 0;

    private Handler recordWaitHandler, recordTimerHandler;
    private Runnable recordRunnable, recordTimerRunnable;
    private MediaRecorder mRecorder = null;
    private String recordFilePath;
    private float displayWidth;
    private boolean callIsVideo;

    private ArrayList<Integer> adapterPositions = new ArrayList<>();

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String currentlyPlaying = "";

    private Toolbar toolbar;
    private RelativeLayout toolbarContent;
    private TextView selectedCount, status, userName;
    private TableLayout addAttachmentLayout;
    private RecyclerView recyclerView;
    private EmojiEditText newMessage;
    private ImageView usersImage, addAttachment, sendMessage, attachment_emoji;
    private LinearLayout rootView, sendContainer, myAttachmentLLY;
    private ImageView callAudio, callVideo;

    private String cameraPhotoPath;
    private EmojiPopup emojIcon;

    private String pickerPath;
    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private FilePicker filePicker;
    private AudioPicker audioPicker;
    private VideoPicker videoPicker;
    private RelativeLayout replyLay;
    private TextView replyName;
    private ImageView replyImg;
    private ImageView closeReply;
    private HashMap<String, User> myUsersNameInPhoneMap;
    private String replyId = "0";
    private TextView userStatus;

    String senderIdDelete = "";
    String recipientIdDelete = "";
    String bodyDelete = "";
    String msgID = "", newMsgID = "";
    long dateDelete;
    private boolean delete = false;
    private boolean deleteGroup = false;
    private ImageView camera;

    //Download complete listener
    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null)
                switch (intent.getAction()) {
                    case DownloadManager.ACTION_DOWNLOAD_COMPLETE:
                        if (adapterPositions.size() > 0 && messageAdapter != null)
                            for (int pos : adapterPositions)
                                if (pos != -1)
                                    messageAdapter.notifyItemChanged(pos);
                        adapterPositions.clear();
                        break;
                }
        }
    };

    //Download event listener
    private BroadcastReceiver downloadEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DownloadFileEvent downloadFileEvent = intent.getParcelableExtra("data");
            if (downloadFileEvent != null) {
                downloadFile(downloadFileEvent);
            }
        }
    };

    @Override
    void myUsersResult(ArrayList<User> myUsers) {

    }

    @Override
    void myContactsResult(ArrayList<Contact> myContacts) {

    }

    @Override
    void userAdded(User valueUser) {
        //do nothing
    }

    @Override
    void groupAdded(Group valueGroup) {
        //do nothing
    }

    @Override
    void userUpdated(User valueUser) {
        if (user != null && user.getId().equals(valueUser.getId())) {
            valueUser.setNameInPhone(user.getNameInPhone());
            user = valueUser;

            //userName.setCompoundDrawablesWithIntrinsicBounds(user.isOnline() ? R.drawable.ring_green : 0, 0, 0, 0);
            status.setText(user.getStatus());
            status.setSelected(true);
            showTyping(user.isTyping());//Show typing
            int existingPos = MainActivity.myUsers.indexOf(valueUser);
            if (existingPos != -1) {
                MainActivity.myUsers.set(existingPos, valueUser);
                helper.setCacheMyUsers(MainActivity.myUsers);
                messageAdapter.notifyDataSetChanged();
            }
        } else if (userMe.getId().equalsIgnoreCase(valueUser.getId())) {
            helper.setLoggedInUser(valueUser);
        } else {
            int existingPos = MainActivity.myUsers.indexOf(valueUser);
            if (existingPos != -1) {
                valueUser.setNameInPhone(MainActivity.myUsers.get(existingPos).getNameToDisplay());
                MainActivity.myUsers.set(existingPos, valueUser);
                helper.setCacheMyUsers(MainActivity.myUsers);
            }
        }
    }

    @Override
    void groupUpdated(Group valueGroup) {
        if (group != null && group.getId().equals(valueGroup.getId())) {
            group = valueGroup;
            checkIfChatAllowed();
        }
    }

    @Override
    void statusAdded(Status status) {

    }

    @Override
    void statusUpdated(Status status) {

    }

    @Override
    void onSinchConnected() {
        callAudio.setClickable(true);
        callVideo.setClickable(true);
    }

    @Override
    void onSinchDisconnected() {
        callAudio.setClickable(false);
        callVideo.setClickable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }
}