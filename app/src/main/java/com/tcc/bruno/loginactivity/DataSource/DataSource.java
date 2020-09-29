package com.tcc.bruno.loginactivity.DataSource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tcc.bruno.loginactivity.DataModel.UsuarioDataModel;
import com.tcc.bruno.loginactivity.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class DataSource extends SQLiteOpenHelper {

    private static final String DB_NAME = "MyDB.sqlite";
    private static final int DB_VERSION = 1;

    Cursor cursor;
    SQLiteDatabase db;


    public DataSource(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(UsuarioDataModel.criarTabela());
        } catch (Exception e) {

            Log.e("Usuario", "DB ERROR:" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insert(String tabela, ContentValues dados) {

        boolean sucesso = true;
        try {

            sucesso = db.insert(tabela, null, dados) > 0;
        } catch (Exception e) {

            sucesso = false;
        }
        return sucesso;
    }

    public boolean deletar(String tabela, int id) {

        boolean sucesso = true;

        sucesso = db.delete(tabela, "id=?",
                new String[]{Integer.toString(id)}) > 0;

        return sucesso;

    }

    public boolean alterar(String tabela, ContentValues dados) {

        boolean sucesso = true;

        int id = dados.getAsInteger("id");

        sucesso = db.update(tabela, dados, "id=?",
                new String[]{Integer.toString(id)}) > 0;

        return sucesso;

    }
/*
    public List<Usuario> getAllUsuario() {
        Usuario obj;

        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT * FROM " + UsuarioDataModel.getTABELA() + "ORDER BY id";

        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            do {
                obj = new Usuario(cursor.getString(1), cursor.getString(2));

                obj.setId(cursor.getString(cursor.getColumnIndex(UsuarioDataModel.getId())));
                obj.setEmail(cursor.getString(cursor.getColumnIndex(UsuarioDataModel.getEmail())));

                lista.add(obj);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return lista;
    }
*/
    public Boolean validaEmail(String checkEmail) {

        String recebe = "";
        Log.e("Email recebido:", " " + checkEmail);
        boolean email = false;

        String sql = "SELECT email FROM Usuario WHERE email = '" + checkEmail + "'";

        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            recebe = (cursor.getString(cursor.getColumnIndex(UsuarioDataModel.getEmail())));


        }
        Log.e("Email recebido:", " SQL " + recebe);

        if (recebe.isEmpty()) {
            email = true;
        } else {
            email = false;
        }


        return email;
    }

    public Usuario queryUser(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();
        Usuario usuario = null;

        cursor = db.query(UsuarioDataModel.getTABELA(), new String[]{UsuarioDataModel.getId(),
                        UsuarioDataModel.getEmail(), UsuarioDataModel.getSenha()}, UsuarioDataModel.getEmail() + "=? and " + UsuarioDataModel.getSenha() + "=?",
                new String[]{email, password}, null, null, null, "1");
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            usuario = new Usuario();
            //cursor.getString(1), cursor.getString(2)
        }
        // return user


        return usuario;
    }


}