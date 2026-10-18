/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

/**
 *
 * @author farid
 */
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class AdvancedResourceCleaner {

    private static final String ICONS_PATH = "src/icon"; 
    private static final String SRC_PATH = "src"; 

    public static void main(String[] args) throws IOException {
        File iconsDir = new File(ICONS_PATH);
        File srcDir = new File(SRC_PATH);

        if (!iconsDir.exists() || !srcDir.exists()) {
            System.out.println("❌ خطأ: يرجى التحقق من مسارات المجلدات!");
            return;
        }

        // جلب ملفات المشروع المتوافقة مع Java 8 Stream
        List<Path> projectFiles = Files.walk(srcDir.toPath())
                .filter(p -> {
                    String name = p.toString().toLowerCase();
                    return name.endsWith(".java") || name.endsWith(".form") || name.endsWith(".txt") || name.endsWith(".json");
                })
                .collect(Collectors.toList());

        File[] icons = iconsDir.listFiles();
        if (icons == null) return;

        int deletedCount = 0;

        for (File icon : icons) {
            if (icon.isDirectory()) continue;

            String fullFileName = icon.getName();
            
            // استخراج اسم الصورة بدون الامتداد (مثال: cashFlow)
            String pureName = fullFileName;
            int lastDotIndex = fullFileName.lastIndexOf('.');
            if (lastDotIndex > 0) {
                pureName = fullFileName.substring(0, lastDotIndex);
            }

            // لتجنب تطابق الأسماء القصيرة جداً مع كلمات برمجية (مثل صورة اسمها "a")
            if (pureName.length() < 3) {
                System.out.println("⚠️ تخطي الفحص التلقائي للأيقونة ذات الاسم القصير جداً لحمايتها: " + fullFileName);
                continue;
            }

            boolean isUsed = false;

            // البحث عن النص المجرّد لاسم الأيقونة داخل كل ملفات المشروع
            for (Path file : projectFiles) {
                try {
                    // التعديل المتوافق مع Java 8 لقراءة الملف بالكامل كنص
                    byte[] bytes = Files.readAllBytes(file);
                    String content = new String(bytes, StandardCharsets.UTF_8);
                    
                    if (content.contains(pureName)) {
                        isUsed = true;
                        break; 
                    }
                } catch (IOException e) {
                    System.out.println("⚠️ تعذر قراءة الملف: " + file.getFileName());
                }
            }

            // حذف فقط إذا لم يُعثر على اسم الصورة نهائياً في أي مكان بالمشروع
            if (!isUsed) {
                if (icon.delete()) {
                    System.out.println("🗑️ تم حذف أيقونة غير مستعملة إطلاقاً: " + fullFileName);
                    deletedCount++;
                } else {
                    System.out.println("⚠️ فشل حذف الأيقونة: " + fullFileName);
                }
            }
        }

        System.out.println("✅ اكتمل الفحص الآمن! تم حذف " + deletedCount + " أيقونة غير مستخدمة.");
    }
}


