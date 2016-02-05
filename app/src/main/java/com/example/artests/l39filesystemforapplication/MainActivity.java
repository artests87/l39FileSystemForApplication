package com.example.artests.l39filesystemforapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button getFileListButton = (Button) findViewById(R.id.buttonGetFileList);
        // final ListView savedFilesListView = (ListView) findViewById(R.id.listViewFileList);
        final ListView savedFilesListView = (ListView) findViewById(R.id.listViewFileList);

        Button saveFileButton = (Button) findViewById(R.id.buttonSave);
        //Button button = (Button) findViewById(R.id.button);
        final EditText fileNameEditText = (EditText) findViewById(R.id.editTextFileName);
        final EditText contentEditText = (EditText) findViewById(R.id.editTextContent);
        Toast.makeText(getApplicationContext(),
                getApplicationContext().getFilesDir().toString(), Toast.LENGTH_LONG).show();
        final Context context=getApplicationContext();
        getFileListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] savedFilesArray = fileList(); // получаем массив имен файлов

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        v.getContext(),
                        android.R.layout.simple_list_item_1, savedFilesArray);
                // выводим список файлов
                savedFilesListView.setAdapter(adapter);
            }
        });
        saveFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = fileNameEditText.getText().toString(); // имя будущего файла
                String content = contentEditText.getText().toString();   // получаем содержимое


                FileOutputStream fos;
                try {
                    fos = openFileOutput(fileName, Context.MODE_APPEND); // открываем файл для записи
                    fos.write(content.getBytes()); // записываем данные
                    fos.close(); // закрываем файл

                    // выводим сообщение
                    Toast.makeText(getApplicationContext(),
                            "Файл " + fileName + " сохранён", Toast.LENGTH_LONG).show();

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(),
                            "{ХЕРНЯ " + fileName + " сохранён", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(),
                            "{ХЕРНЯ " + fileName + " сохранён", Toast.LENGTH_LONG).show();
                }
            }
        });
        savedFilesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String clickedFile = (String) parent
                        .getItemAtPosition(position);
                openFileDialog(clickedFile);
            }
        });
        savedFilesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent,
                                           View view, int position, long id) {
                String longClickedFile = (String) parent
                        .getItemAtPosition(position);
                // удаляем выбранный файл
                deleteFile(longClickedFile);
                Toast.makeText(getApplicationContext(),
                        longClickedFile + " удалён", Toast.LENGTH_LONG)
                        .show();
                showSavedFiles();

                return true;
            }
        });
    }
    private void openFileDialog(String file) {
        // чтение файла из файлового хранилища
        FileInputStream fis;
        String content = "";
        try {
            fis = openFileInput(file);  // открываем файл для чтения
            byte[] input = new byte[fis.available()];
            while (fis.read(input) != -1) {
                content += new String(input);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // создаем собственное диалоговое окно
        AlertDialog.Builder fileDialog = new AlertDialog.Builder(
                this);
        fileDialog.setTitle(file);

        TextView contentTextView = new TextView(this);
        contentTextView.setText(content);
        ViewGroup.LayoutParams textViewLayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentTextView.setLayoutParams(textViewLayoutParams);

        fileDialog.setView(contentTextView);
        fileDialog.setPositiveButton("OK", null);
        fileDialog.show();
    }
    private void showSavedFiles() {
        ListView savedFilesListView = (ListView) findViewById(R.id.listViewFileList);
        String[] savedFilesArray = getApplicationContext().fileList();
        ArrayAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, savedFilesArray);

        savedFilesListView.setAdapter(adapter);
    }




}
