package com.example.cropncapture;




import java.util.List;

import com.example.binarizao.Binarization;
import com.example.binarizao.Divisor;

import android.support.v7.app.ActionBarActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity implements OnClickListener{
	//keep track of all intents
	final int CAMERA_CAPTURE = 1;
	final int PIC_CROP = 2;
	final int SELECT_PICTURE = 3;
	//captured picture uri and bitmap
	private Uri picUri;
	private Bitmap img;
	private ImageView imageView;
	private ImageView imageView2;
	private int letra;
	List <Bitmap> letras;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//retrieve a reference to the UI button
		Button captureBtn = (Button)findViewById(R.id.capture_btn);
		//handle button clicks
		captureBtn.setOnClickListener(this);
		captureBtn = (Button)findViewById(R.id.bin_btn);
		captureBtn.setOnClickListener(this);
		captureBtn = (Button) findViewById(R.id.capFile_btn);
		captureBtn.setOnClickListener(this);
		captureBtn = (Button) findViewById(R.id.ant_btn);
		captureBtn.setOnClickListener(this);
		captureBtn = (Button) findViewById(R.id.prox_btn);
		captureBtn.setOnClickListener(this);
		imageView = (ImageView)findViewById(R.id.picture);
		imageView2 = (ImageView) findViewById(R.id.picture2);
		
	}

	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		switch(v.getId()){
		case R.id.capture_btn:
				try {
				    //use standard intent to capture an image
				    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				    //we will handle the returned data in onActivityResult
				    startActivityForResult(captureIntent, CAMERA_CAPTURE);
				}catch(ActivityNotFoundException anfe){
				    //display an error message
				    String errorMessage = "Whoops - your device doesn't support capturing images!";
				    Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
				    toast.show();
				}
			break;
		case R.id.bin_btn: 
			/*	Kmeans2 k = new Kmeans2(img, 3);
				k.initGroupsP();
				k.gerarProtosMediana();
				k.runKmeansChebyshev();*/
				
				Binarization b;
				b = new Binarization(img);
				b.dinamicCluster2();	
			//	b.Gradient();
				//img = b.getImage();
			//	Divisor d = new Divisor(img);
				//Bitmap letra = Divisor.dividirMEAN(img);
				imageView.setImageBitmap(b.getImage());
				 letras = Divisor.dividirMEAN(b.getImage());
				 if(letras.size()<8){
					 b.dinamicCluster2();
					 letras = Divisor.dividirMEAN(b.getImage());
				 }
				imageView2.setImageBitmap(letras.get(0));
				letra = 0;
				//ImageView im = (ImageView) findViewById(R.id.picture2);
				//im.setImageBitmap(letra);
				//im.setImageBitmap(letra);
				aviso("qtdLetras: " + letras.size());
			/*	Button temp = (Button) findViewById(R.id.ant_btn);
				temp.setEnabled(true);
				temp = (Button) findViewById(R.id.prox_btn);
				temp.setEnabled(true);*/
			break;
		case R.id.capFile_btn:
				 Intent intent = new Intent();
	             intent.setType("image/*");
	             intent.setAction(Intent.ACTION_GET_CONTENT);
	             startActivityForResult(Intent.createChooser(intent,
	                     "Select Picture"), SELECT_PICTURE);
			break;
		case R.id.ant_btn:
				if(letra > 0)
					imageView2.setImageBitmap(letras.get(--letra));
				
			break;
		case R.id.prox_btn:
			if(letra < letras.size()-1)
				imageView2.setImageBitmap(letras.get(++letra));
			break;
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == RESULT_OK) {
	    	if(requestCode == CAMERA_CAPTURE){
	    		picUri = data.getData();
	    		performCrop();
	    	}
	    	else if(requestCode == PIC_CROP){
		    	//get the returned data
		    	Bundle extras = data.getExtras();
		    	//get the cropped bitmap
		    	img = extras.getParcelable("data");
		    	//retrieve a reference to the ImageView
		    	ImageView picView = (ImageView)findViewById(R.id.picture);
		    	//display the returned cropped image
		    	picView.setImageBitmap(img);
		    }
	    	else if(requestCode == SELECT_PICTURE){
	    		picUri = data.getData();
	    		performCrop();
	    	/*	String [] projection = {MediaStore.Images.Media.DATA};
	    		Cursor cursor = getContentResolver().query(picUri, projection, null, null, null);
	    		cursor.moveToFirst();
	    		int columnIndex = cursor.getColumnIndex(projection[0]);
                String selectedImagePath = cursor.getString(columnIndex);
                cursor.close();
                img = BitmapFactory.decodeFile(selectedImagePath);
                //retrieve a reference to the ImageView
		    	ImageView picView = (ImageView)findViewById(R.id.picture);
		    	//display the returned cropped image
		    	picView.setImageBitmap(img);*/
	    	}
	    }
	    
	}
	private void performCrop(){
		try {
			//call the standard crop action intent (the user device may not support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP"); 
			    //indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			    //set crop properties
			cropIntent.putExtra("crop", "true");
			    //indicate aspect of desired crop
		/*	cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);*/
			    //indicate output X and Y
			cropIntent.putExtra("outputX", 256);
			cropIntent.putExtra("outputY", 256);
			    //retrieve data on return
			cropIntent.putExtra("return-data", true);
			    //start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, PIC_CROP);
		}
		catch(ActivityNotFoundException anfe){
		    //display an error message
		    String errorMessage = "Whoops - your device doesn't support the crop action!";
		    Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
		    toast.show();
		}
	}
    private void aviso(String s) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
         //define o titulo
        builder.setTitle("AVISO");
         //define a mensagem
        builder.setMessage(s);
        //define um botão como positivo
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1){
             //   Toast.makeText(MainActivity.this, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });        
        AlertDialog alerta = builder.create();
        alerta.show();
    }
	
}
