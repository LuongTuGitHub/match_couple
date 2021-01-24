package com.java.tu.app.message.asset;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.java.tu.app.message.R;
import com.java.tu.app.message.database.sqlite.AssetImage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.java.tu.app.message.asset.Const.IMAGE;
import static com.java.tu.app.message.asset.Const.KEY;

public class Image {

    private final StorageReference refStg;
    private final AssetImage asset;
    private final Context context;

    public Image(@NotNull Context context) {
        refStg = FirebaseStorage.getInstance().getReference();
        this.context = context;
        this.asset = new AssetImage(context);
    }

    public void getImage(@NotNull ImageView image, @NotNull String target, long value) {
        if (asset.checkExist(target)) {
            byte[] bytes = asset.getImage(target);
            if (bytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                image.setImageBitmap(bitmap);
            }
        } else {
            refStg.child(IMAGE + "/" + target + KEY).getBytes(value).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                @SuppressLint("UseCompatLoadingForDrawables")
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
            });
        }
    }
}
