package de.dtonal.knitandcount.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class FileUtil {
    private static void createFolderIfNotExists(File folder) {
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public static File getOrCreateProjectFolder(Context applicationContext, int project_id) {
        File directory = applicationContext.getFilesDir();
        File projectsDir = new File(directory, String.valueOf(project_id));
        FileUtil.createFolderIfNotExists(projectsDir);
        return projectsDir;
    }

    public static void copyChosenDataContentToFile(ContentResolver contentResolver, Uri data, File copy) throws IOException {
        try (InputStream in = contentResolver.openInputStream(data); OutputStream out = new BufferedOutputStream(
                new FileOutputStream(copy))) {
            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = Objects.requireNonNull(in).read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
        }
    }
}
