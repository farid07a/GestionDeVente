/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ArchiveSuppression {

    private static final String FILE_PATH = "resources/suppression.txt";

    // =========================================================
    // ARCHIVER
    // =========================================================
    public static void archiver(Object object) {

        if (object == null) {
            return;
        }

        File file = new File(FILE_PATH);

        try {

            if (!file.exists()) {

                File parent = file.getParentFile();

                if (parent != null) {
                    parent.mkdirs();
                }

                file.createNewFile();
            }

            String dateSuppression
                    = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            String data = objectToString(object);

            try ( BufferedWriter writer
                    = new BufferedWriter(new FileWriter(file, true))) {

                writer.write(
                        object.getClass().getSimpleName()
                        + "{"
                        + data
                        + "} : "
                        + dateSuppression
                );

                writer.newLine();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // =========================================================
    // تحويل OBJECT إلى STRING
    // =========================================================
    private static String objectToString(Object object) {

        if (object == null) {
            return "null";
        }

        Class<?> clazz = object.getClass();

        if (isSimpleType(clazz)) {
            return String.valueOf(object);
        }

        StringBuilder data = new StringBuilder();

        // =====================================================
        // 1. البحث عن ID
        // =====================================================
        Field idField = findIdField(clazz);

        if (idField != null) {

            try {

                idField.setAccessible(true);

                Object idValue = idField.get(object);

                data.append("id=")
                        .append(idValue);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // =====================================================
        // 2. باقي FIELDS
        // =====================================================
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {

            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            // ما نعاودوش ID
            if (field == idField) {
                continue;
            }

            try {

                field.setAccessible(true);

                Object value = field.get(object);

                if (data.length() > 0) {
                    data.append(", ");
                }

                data.append(field.getName())
                        .append("=");

                if (value == null) {

                    data.append("null");

                } else if (isSimpleType(value.getClass())) {

                    data.append(value);

                } else {

                    data.append(value.getClass().getSimpleName())
                            .append("{")
                            .append(objectToString(value))
                            .append("}");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return data.toString();
    }

    // =========================================================
    // البحث عن ID
    // =========================================================
    private static Field findIdField(Class<?> clazz) {

        Class<?> currentClass = clazz;

        while (currentClass != null) {

            Field[] fields = currentClass.getDeclaredFields();

            for (Field field : fields) {

                String name = field.getName().toLowerCase();

                if (name.equals("id")) {
                    return field;
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        return null;
    }

    // =========================================================
    // SIMPLE TYPE
    // =========================================================
    private static boolean isSimpleType(Class<?> clazz) {

        return clazz.isPrimitive()
                || clazz == String.class
                || clazz == Integer.class
                || clazz == Long.class
                || clazz == Double.class
                || clazz == Float.class
                || clazz == Boolean.class
                || clazz == Short.class
                || clazz == Byte.class
                || clazz == Character.class
                || clazz == Date.class
                || clazz == LocalDate.class
                || clazz.isEnum();
    }

    // =========================================================
    // GET NESTED VALUE
    // =========================================================
    private static String getNestedValue(
            String data,
            String... fields) {

        String currentData = data;

        for (int i = 0; i < fields.length - 1; i++) {

            String objectContent
                    = getObjectContent(
                            currentData,
                            fields[i]);

            if (objectContent.isEmpty()) {
                return "";
            }

            currentData = objectContent;
        }

        return getValue(
                currentData,
                fields[fields.length - 1]);
    }

    // =========================================================
    // GET OBJECT CONTENT
    // =========================================================
    private static String getObjectContent(
            String data,
            String fieldName) {

        int niveau = 0;

        String search = fieldName + "=";

        for (int i = 0;
                i <= data.length() - search.length();
                i++) {

            char c = data.charAt(i);

            if (c == '{') {
                niveau++;
            } else if (c == '}') {
                niveau--;
            }

            // نبحث فقط في المستوى الرئيسي
            if (niveau == 0
                    && data.startsWith(search, i)) {

                int start = i + search.length();

                int openBrace
                        = data.indexOf("{", start);

                if (openBrace == -1) {
                    return "";
                }

                int depth = 1;

                int end = openBrace + 1;

                while (end < data.length()
                        && depth > 0) {

                    char current
                            = data.charAt(end);

                    if (current == '{') {
                        depth++;
                    } else if (current == '}') {
                        depth--;
                    }

                    end++;
                }

                if (depth == 0) {

                    return data.substring(
                            openBrace + 1,
                            end - 1);
                }
            }
        }

        return "";
    }

    // =========================================================
    // TABLE NORMAL
    // =========================================================
    public static void afficherDansTable(JTable table) {

        DefaultTableModel model
                = new DefaultTableModel(
                        new Object[]{
                            "Type",
                            "ID",
                            "Informations",
                            "Date suppression"
                        }, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };

        table.setModel(model);

        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return;
        }

        try ( BufferedReader reader
                = new BufferedReader(
                        new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                int firstBrace
                        = line.indexOf("{");

                int lastBrace
                        = line.lastIndexOf("}");

                int separator
                        = line.lastIndexOf(" : ");

                if (firstBrace == -1
                        || lastBrace == -1
                        || separator == -1) {

                    continue;
                }

                String type
                        = line.substring(
                                0,
                                firstBrace)
                                .trim();

                String data
                        = line.substring(
                                firstBrace + 1,
                                lastBrace)
                                .trim();

                String dateSuppression
                        = line.substring(
                                separator + 3)
                                .trim();

                String id
                        = extraireIdPrincipal(data);

                model.addRow(
                        new Object[]{
                            type,
                            id,
                            data,
                            dateSuppression
                        });
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // =========================================================
    // TABLE ARABE
    // =========================================================
    public static void afficherDansTableArabe(
            JTable table) {

        DefaultTableModel model
                = new DefaultTableModel(new Object[]{"تاريخ الحذف", "العنصر المحذوف", "النوع",}, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };

        table.setModel(model);

        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return;
        }

        try ( BufferedReader reader
                = new BufferedReader(
                        new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                int firstBrace
                        = line.indexOf("{");

                int lastBrace
                        = line.lastIndexOf("}");

                int separator
                        = line.lastIndexOf(" : ");

                if (firstBrace == -1
                        || lastBrace == -1
                        || separator == -1) {

                    continue;
                }

                String type
                        = line.substring(
                                0,
                                firstBrace)
                                .trim();

                String data
                        = line.substring(
                                firstBrace + 1,
                                lastBrace)
                                .trim();

                String dateSuppression
                        = line.substring(
                                separator + 3)
                                .trim();

                String typeArabe
                        = getTypeArabe(type);

                String resume = getResumeArabe(type, data);
                model.insertRow(0, new Object[]{dateSuppression, resume, typeArabe});
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // =========================================================
    // TABLE FRANÇAIS
    // =========================================================
    public static void afficherDansTableFrancais(
            JTable table) {

        DefaultTableModel model
                = new DefaultTableModel(
                        new Object[]{
                            "Type",
                            "Élément supprimé",
                            "Date de suppression"
                        }, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };

        table.setModel(model);

        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return;
        }

        try ( BufferedReader reader
                = new BufferedReader(
                        new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                int firstBrace
                        = line.indexOf("{");

                int lastBrace
                        = line.lastIndexOf("}");

                int separator
                        = line.lastIndexOf(" : ");

                if (firstBrace == -1
                        || lastBrace == -1
                        || separator == -1) {

                    continue;
                }

                String type
                        = line.substring(
                                0,
                                firstBrace)
                                .trim();

                String data
                        = line.substring(
                                firstBrace + 1,
                                lastBrace)
                                .trim();

                String dateSuppression
                        = line.substring(
                                separator + 3)
                                .trim();

                String typeFrancais
                        = getTypeFrancais(type);

                String resume
                        = getResumeFrancais(
                                type,
                                data);

                model.addRow(
                        new Object[]{
                            typeFrancais,
                            resume,
                            dateSuppression
                        });
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // =========================================================
    // TYPE ARABE
    // =========================================================
    private static String getTypeArabe(
            String type) {

        switch (type) {

            case "Client":
                return "زبون";

            case "ClientPayeParEntreprise":
                return "زبون مدفوع من طرف المؤسسة";

            case "Utilisateur":
                return "مستخدم";

            case "Entreprise":
                return "مؤسسة";

            case "Produit":
                return "منتج";

            case "Categorie":
            case "Catégorie":
                return "فئة";

            case "Achat":
                return "مشتريات";

            case "AchatDetail":
                return "تفاصيل المشتريات";

            case "Versement":
                return "تسديد";

            case "Versement_Entreprise":
            case "VersementEntreprise":
                return "تسديد المؤسسة";

            default:
                return type;
        }
    }

    // =========================================================
    // TYPE FRANÇAIS
    // =========================================================
    private static String getTypeFrancais(
            String type) {

        switch (type) {

            case "Client":
                return "Client";

            case "ClientPayeParEntreprise":
                return "Client payé par entreprise";

            case "Utilisateur":
                return "Utilisateur";

            case "Entreprise":
                return "Entreprise";

            case "Produit":
                return "Produit";

            case "Categorie":
            case "Catégorie":
                return "Catégorie";

            case "Achat":
                return "Achat";

            case "AchatDetail":
                return "Détail achat";

            case "Versement":
                return "Versement";

            case "Versement_Entreprise":
            case "VersementEntreprise":
                return "Versement entreprise";

            default:
                return type;
        }
    }

    // =========================================================
    // RESUME ARABE
    // =========================================================
    private static String getResumeArabe(
            String type,
            String data) {

        switch (type) {

            // =================================================
            // CLIENT
            // =================================================
            case "Client": {

                String nom = getValue(data, "nom");
                String prenom = getValue(data, "prenom");

                String entreprise = getNestedValue(
                        data,
                        "entreprise",
                        "nom_ar"
                );

                if (entreprise.isEmpty()) {

                    entreprise = getNestedValue(
                            data,
                            "entreprise",
                            "nom_fr"
                    );
                }

                String resultat = "";

                // الاسم واللقب
                if (!nom.isEmpty()) {
                    resultat = nom;
                }

                if (!prenom.isEmpty()) {
                    resultat += " " + prenom;
                }

                // المؤسسة
                if (!entreprise.isEmpty()) {
                    resultat += " — المؤسسة: " + entreprise;
                }

                return resultat;
            }

            // =================================================
            // CLIENT PAYE PAR ENTREPRISE
            // =================================================
            case "ClientPayeParEntreprise": {

                String achatId
                        = getNestedValue(
                                data,
                                "achat",
                                "id");

                String nom
                        = getNestedValue(
                                data,
                                "achat",
                                "client",
                                "nom");

                String prenom
                        = getNestedValue(
                                data,
                                "achat",
                                "client",
                                "prenom");

                String entreprise
                        = getNestedValue(
                                data,
                                "achat",
                                "client",
                                "entreprise",
                                "nom_ar");

                if (entreprise.isEmpty()) {

                    entreprise
                            = getNestedValue(
                                    data,
                                    "achat",
                                    "client",
                                    "entreprise",
                                    "nom_fr");
                }

                String montant
                        = getNestedValue(
                                data,
                                "versementEntreprise",
                                "montant");

                String resultat
                        = "الزبون";

                if (!nom.isEmpty()
                        || !prenom.isEmpty()) {

                    resultat
                            += ": "
                            + nom
                            + " "
                            + prenom;
                }

                if (!entreprise.isEmpty()) {

                    resultat
                            += " — المؤسسة: "
                            + entreprise;
                }

                if (!achatId.isEmpty()) {

                    resultat
                            += " — المشتريات رقم "
                            + achatId;
                }

                if (!montant.isEmpty()) {

                    resultat
                            += " — المبلغ: "
                            + montant
                            + " دج";
                }

                return resultat;
            }

            // =================================================
            // UTILISATEUR
            // =================================================
            case "Utilisateur": {

                String nom
                        = getValue(data, "nom");

                return "المستخدم: " + nom;
            }

            // =================================================
            // ENTREPRISE
            // =================================================
            case "Entreprise": {

                String nomAr
                        = getValue(data, "nom_ar");

                String nomFr
                        = getValue(data, "nom_fr");

                if (!nomAr.isEmpty()) {
                    return nomAr;
                }

                return nomFr;
            }

            // =================================================
            // PRODUIT
            // =================================================
            case "Produit": {

                String designation
                        = getValue(
                                data,
                                "designation");

                String reference
                        = getValue(
                                data,
                                "reference");

                String categorie
                        = getNestedValue(
                                data,
                                "categorie",
                                "nomCategorie");

                String resultat
                        = designation;

//                if (!reference.isEmpty()) {
//
//                    resultat
//                            += " — المنتج: "
//                            + reference;
//                }
                if (!categorie.isEmpty()) {

                    resultat
                            += " — الفئة: "
                            + categorie;
                }

                return resultat;
            }

            // =================================================
            // CATEGORIE
            // =================================================
            case "Categorie":
            case "Catégorie":

                return getValue(
                        data,
                        "nomCategorie");

            // =================================================
            // ACHAT
            // =================================================
            case "Achat": {

                String id
                        = getValue(
                                data,
                                "id");

                String nom
                        = getNestedValue(
                                data,
                                "client",
                                "nom");

                String prenom
                        = getNestedValue(
                                data,
                                "client",
                                "prenom");

                String entreprise
                        = getNestedValue(
                                data,
                                "client",
                                "entreprise",
                                "nom_ar");

                if (entreprise.isEmpty()) {

                    entreprise
                            = getNestedValue(
                                    data,
                                    "client",
                                    "entreprise",
                                    "nom_fr");
                }

                String total
                        = getValue(
                                data,
                                "prix_total");

                String resultat
                        = "مشتريات رقم "
                        + id;

                if (!nom.isEmpty()
                        || !prenom.isEmpty()) {

                    resultat
                            += " — الزبون: "
                            + nom
                            + " "
                            + prenom;
                }

                if (!entreprise.isEmpty()) {

                    resultat
                            += " — المؤسسة: "
                            + entreprise;
                }

                if (!total.isEmpty()) {

                    resultat
                            += " — المجموع: "
                            + total
                            + " دج";
                }

                return resultat;
            }

            // =================================================
            // ACHAT DETAIL
            // =================================================
            case "AchatDetail": {

                String idAchat
                        = getValue(
                                data,
                                "id_achat");

                String produit
                        = getNestedValue(
                                data,
                                "produit",
                                "designation");

                String qty
                        = getValue(
                                data,
                                "qty");

                String total
                        = getValue(
                                data,
                                "prix_total");

                return "تفاصيل المشتريات رقم "
                        + idAchat
                        + " — المنتج: "
                        + produit
                        + " — الكمية: "
                        + qty
                        + " — المجموع: "
                        + total
                        + " دج";
            }

            // =================================================
            // VERSEMENT
            // =================================================
            case "Versement":
            case "Versement_Entreprise":
            case "VersementEntreprise": {

                String entreprise
                        = getNestedValue(
                                data,
                                "entreprise",
                                "nom_ar");

                if (entreprise.isEmpty()) {

                    entreprise
                            = getNestedValue(
                                    data,
                                    "entreprise",
                                    "nom_fr");
                }

                String montant
                        = getValue(
                                data,
                                "montant");

                if (!entreprise.isEmpty()) {

                    return "المؤسسة: "
                            + entreprise
                            + " — المبلغ: "
                            + montant
                            + " دج";
                }

                return "المبلغ: "
                        + montant
                        + " دج";
            }

            default:
                return data;
        }
    }

    // =========================================================
    // RESUME FRANÇAIS
    // =========================================================
    private static String getResumeFrancais(
            String type,
            String data) {

        switch (type) {

            // =================================================
            // CLIENT
            // =================================================
            case "Client": {

                String nom
                        = getValue(data, "nom");

                String prenom
                        = getValue(data, "prenom");

                String entreprise
                        = getNestedValue(
                                data,
                                "entreprise",
                                "nom_fr");

                if (entreprise.isEmpty()) {

                    entreprise
                            = getNestedValue(
                                    data,
                                    "entreprise",
                                    "nom_ar");
                }

                String resultat
                        = nom + " " + prenom;

                if (!entreprise.isEmpty()) {

                    resultat
                            += " — Entreprise : "
                            + entreprise;
                }

                return resultat;
            }

            // =================================================
            // CLIENT PAYE PAR ENTREPRISE
            // =================================================
            case "ClientPayeParEntreprise": {

                String achatId
                        = getNestedValue(
                                data,
                                "achat",
                                "id");

                String nom
                        = getNestedValue(
                                data,
                                "achat",
                                "client",
                                "nom");

                String prenom
                        = getNestedValue(
                                data,
                                "achat",
                                "client",
                                "prenom");

                String entreprise
                        = getNestedValue(
                                data,
                                "achat",
                                "client",
                                "entreprise",
                                "nom_fr");

                if (entreprise.isEmpty()) {

                    entreprise
                            = getNestedValue(
                                    data,
                                    "achat",
                                    "client",
                                    "entreprise",
                                    "nom_ar");
                }

                String montant
                        = getNestedValue(
                                data,
                                "versementEntreprise",
                                "montant");

                String resultat
                        = "Client";

                if (!nom.isEmpty()
                        || !prenom.isEmpty()) {

                    resultat
                            += " : "
                            + nom
                            + " "
                            + prenom;
                }

                if (!entreprise.isEmpty()) {

                    resultat
                            += " — Entreprise : "
                            + entreprise;
                }

                if (!achatId.isEmpty()) {

                    resultat
                            += " — Achat N° "
                            + achatId;
                }

                if (!montant.isEmpty()) {

                    resultat
                            += " — Montant : "
                            + montant
                            + " DA";
                }

                return resultat;
            }

            // =================================================
            // UTILISATEUR
            // =================================================
            case "Utilisateur": {

                String nom
                        = getValue(data, "nom");

                return "Utilisateur : " + nom;
            }

            // =================================================
            // ENTREPRISE
            // =================================================
            case "Entreprise": {

                String nomFr
                        = getValue(
                                data,
                                "nom_fr");

                String nomAr
                        = getValue(
                                data,
                                "nom_ar");

                if (!nomFr.isEmpty()) {
                    return nomFr;
                }

                return nomAr;
            }

            // =================================================
            // PRODUIT
            // =================================================
            case "Produit": {

                String designation
                        = getValue(
                                data,
                                "designation");

                String reference
                        = getValue(
                                data,
                                "reference");

                String categorie
                        = getNestedValue(
                                data,
                                "categorie",
                                "nomCategorie");

                String resultat
                        = designation;

                if (!reference.isEmpty()) {

                    resultat
                            += " — Réf : "
                            + reference;
                }

                if (!categorie.isEmpty()) {

                    resultat
                            += " — Catégorie : "
                            + categorie;
                }

                return resultat;
            }

            // =================================================
            // CATEGORIE
            // =================================================
            case "Categorie":
            case "Catégorie":

                return getValue(
                        data,
                        "nomCategorie");

            // =================================================
            // ACHAT
            // =================================================
            case "Achat": {

                String id
                        = getValue(
                                data,
                                "id");

                String nom
                        = getNestedValue(
                                data,
                                "client",
                                "nom");

                String prenom
                        = getNestedValue(
                                data,
                                "client",
                                "prenom");

                String entreprise
                        = getNestedValue(
                                data,
                                "client",
                                "entreprise",
                                "nom_fr");

                if (entreprise.isEmpty()) {

                    entreprise
                            = getNestedValue(
                                    data,
                                    "client",
                                    "entreprise",
                                    "nom_ar");
                }

                String total
                        = getValue(
                                data,
                                "prix_total");

                String resultat
                        = "Achat N° "
                        + id;

                if (!nom.isEmpty()
                        || !prenom.isEmpty()) {

                    resultat
                            += " — Client : "
                            + nom
                            + " "
                            + prenom;
                }

                if (!entreprise.isEmpty()) {

                    resultat
                            += " — Entreprise : "
                            + entreprise;
                }

                if (!total.isEmpty()) {

                    resultat
                            += " — Total : "
                            + total
                            + " DA";
                }

                return resultat;
            }

            // =================================================
            // ACHAT DETAIL
            // =================================================
            case "AchatDetail": {

                String idAchat
                        = getValue(
                                data,
                                "id_achat");

                String produit
                        = getNestedValue(
                                data,
                                "produit",
                                "designation");

                String qty
                        = getValue(
                                data,
                                "qty");

                String total
                        = getValue(
                                data,
                                "prix_total");

                return "Détail achat N° "
                        + idAchat
                        + " — Produit : "
                        + produit
                        + " — Quantité : "
                        + qty
                        + " — Total : "
                        + total
                        + " DA";
            }

            // =================================================
            // VERSEMENT
            // =================================================
            case "Versement":
            case "Versement_Entreprise":
            case "VersementEntreprise": {

                String entreprise
                        = getNestedValue(
                                data,
                                "entreprise",
                                "nom_fr");

                if (entreprise.isEmpty()) {

                    entreprise
                            = getNestedValue(
                                    data,
                                    "entreprise",
                                    "nom_ar");
                }

                String montant
                        = getValue(
                                data,
                                "montant");

                if (!entreprise.isEmpty()) {

                    return "Entreprise : "
                            + entreprise
                            + " — Montant : "
                            + montant
                            + " DA";
                }

                return "Montant : "
                        + montant
                        + " DA";
            }

            default:
                return data;
        }
    }

    // =========================================================
    // GET VALUE
    // =========================================================
    private static String getValue(
            String data,
            String fieldName) {

        int niveau = 0;

        String search
                = fieldName + "=";

        for (int i = 0;
                i <= data.length() - search.length();
                i++) {

            char c = data.charAt(i);

            if (c == '{') {
                niveau++;
            } else if (c == '}') {
                niveau--;
            }

            if (niveau == 0
                    && data.startsWith(
                            search,
                            i)) {

                int start
                        = i + search.length();

                int end = start;

                int nested = 0;

                while (end < data.length()) {

                    char current
                            = data.charAt(end);

                    if (current == '{') {
                        nested++;
                    } else if (current == '}') {
                        nested--;
                    }

                    if (current == ','
                            && nested == 0) {

                        break;
                    }

                    end++;
                }

                return data.substring(
                        start,
                        end)
                        .trim();
            }
        }

        return "";
    }

    // =========================================================
    // EXTRAIRE ID PRINCIPAL
    // =========================================================
    private static String extraireIdPrincipal(
            String data) {

        int niveau = 0;

        for (int i = 0;
                i < data.length();
                i++) {

            char c = data.charAt(i);

            if (c == '{') {
                niveau++;
            }

            if (c == '}') {
                niveau--;
            }

            // ID الموجود في المستوى الرئيسي
            if (niveau == 0
                    && data.startsWith(
                            "id=",
                            i)) {

                int start
                        = i + 3;

                int end = start;

                while (end < data.length()
                        && data.charAt(end)
                        != ',') {

                    end++;
                }

                return data.substring(
                        start,
                        end)
                        .trim();
            }
        }

        return "";
    }

    public static void main(String[] args) {

        Entreprise entreprise = new Entreprise(3, "سيلاس", "cilas", "des", "branis", "033 452578  ", "cilas@gmail.com");
        Client client = new Client(2, "basma", "benaissa", "124568", "0213546", "branise", entreprise);
        Achat achat = new Achat(1, client, 100, LocalDate.now());
        //  new ArchiveSuppression().archiver(achat);
        Utilisateur u = new Utilisateur(6, "admin", "Admin");
        new ArchiveSuppression().archiver(u);

    }
}
