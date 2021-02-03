package com.java.tu.app.message.asset;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.java.tu.app.message.R;
import com.java.tu.app.message.database.sqlite.AssetImage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

import static com.java.tu.app.message.asset.Const.IMAGE;
import static com.java.tu.app.message.asset.Const.KEY;

public class Image {

    private final StorageReference refStg;
    private final AssetImage asset;
    private final Context context;

    /**
     * @param context context
     */

    public Image(@NotNull Context context) {/// @
        this.refStg = FirebaseStorage.getInstance().getReference();
        this.context = context;
        this.asset = new AssetImage(context);
    }


    /***
     *
     * @param uri usi parse from gallery phone
     * @param target target push to firebase storage
     * @param onCompleteListener nullable when complete upload
     *
     * @return void
     */

    public void putImage(@NotNull Uri uri, @NotNull String target, @Nullable OnCompleteListener<UploadTask.TaskSnapshot> onCompleteListener) {
        refStg.child(IMAGE).child(target + KEY).putFile(uri).addOnCompleteListener(onCompleteListener);
    }

    /***
     * @return void
     */

    public void putImage(@NotNull byte[] bytes, @NotNull String target, @Nullable OnCompleteListener<UploadTask.TaskSnapshot> onCompleteListener) {
        refStg.child(IMAGE).child(target + KEY).putBytes(bytes).addOnCompleteListener(onCompleteListener);
    }

    /***
     * @return void
     */

    public void putImage(@NotNull InputStream stream, @NotNull String target, @Nullable OnCompleteListener<UploadTask.TaskSnapshot> onCompleteListener) {
        refStg.child(IMAGE).child(target + KEY).putStream(stream).addOnCompleteListener(onCompleteListener);
    }


    /***
     * @return void set ImageView from firebase storage
     */

    @SuppressLint("UseCompatLoadingForDrawables")
    public void getImage(@NotNull ImageView image, @Nullable String target, long value) {
        if (target == null) {
            image.setImageDrawable(context.getResources().getDrawable(R.color.white));
        } else {
            if (asset.checkExist(target)) {
                byte[] bytes = asset.getImage(target);
                if (bytes != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    image.setImageBitmap(bitmap);
                }
            } else {
                refStg.child(IMAGE + "/" + target + KEY).getBytes(value)
                        .addOnCompleteListener(new OnCompleteListener<byte[]>() {
                            @Override
                            public void onComplete(@NonNull Task<byte[]> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult() != null) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length);
                                        image.setImageBitmap(bitmap);
                                        asset.add(target, task.getResult());
                                    }
                                } else {
                                    image.setImageDrawable(context.getResources().getDrawable(R.color.white));
                                }
                            }
                        })
                        .addOnCanceledListener(new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                image.setImageDrawable(context.getResources().getDrawable(R.color.white));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.fillInStackTrace();
                                image.setImageDrawable(context.getResources().getDrawable(R.color.white));
                            }
                        });
            }
        }
    }
}
