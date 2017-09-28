package com.example.tnrkd.colormakey;

public class GradientDescent {

	public static float[][] X_data= {
			{0,255,255,255,3},
			{255,0,255,255,3},
			{255,255,0,255,3}};
	public static float[][] Y_data= {
			{65},
			{172},
			{106}};
	public static float[][] W={
			{1},
			{1},
			{1},
			{1},
			{(float)0.3}};
	
	public GradientDescent(float[][] X_data,float[][] Y_data){
		this.X_data=X_data;
		this.Y_data=Y_data;
		int row = this.X_data[0].length; //5
		int col = this.Y_data[0].length; //1
		this.W = new float[row][col];
		float[] bright=new float[row];
		
		float tempmin;
		float tempmax;
		if(this.Y_data[0][0]>this.Y_data[1][0]) {
			tempmin=this.Y_data[1][0];
			tempmax=this.Y_data[0][0];
		}
		else {
			tempmin=this.Y_data[0][0];
			tempmax=this.Y_data[1][0];
		}
		if(this.Y_data[2][0]<tempmin)
			tempmin=this.Y_data[2][0];
		if(this.Y_data[2][0]>tempmax)
			tempmax=this.Y_data[2][0];
		float tarbright=(tempmin+tempmax)/(2*255);
		
		for (int i = 0; i < row; i++) {
			float min;
			float max;
			if(this.X_data[0][i]>this.X_data[1][i]) {
				min=this.X_data[1][i];
				max=this.X_data[0][i];
			}
			else {
				min=this.X_data[0][i];
				max=this.X_data[1][i];
			}
			if(this.X_data[2][i]<min)
				min=this.X_data[2][i];
			if(this.X_data[2][i]>max)
				max=this.X_data[2][i];
			bright[i]=(min+max)/(2*255);
			
			if(bright[i]>=tarbright) {
				W[i][0]=1;
			}
			else {
				W[i][0]=tarbright;
			}
		}
	}
	
	public static float[][] MatMul(){
		float[][] result= new float[X_data.length][W[0].length];
		for (int i = 0; i < W[0].length; i++) {
			for (int j = 0; j < X_data.length; j++) {
				float sum=0;	
				for (int k = 0; k < X_data[0].length; k++) {
					sum+=X_data[j][k]*W[k][i];
				}
				result[j][i]=sum;
			}
		}
		
		return result;
	}
	public static float calCost(float[][] H) {
		float result=0;
		int row=H.length;
		int col=H[0].length;
		for (int i = 0; i < H.length; i++) {
			for (int j = 0; j < H[0].length; j++) {
				result+=(float)Math.pow(H[i][j]-Y_data[i][j], 2)/(float)(row*col);
			}
		}
		return result/2;
	}
	public static float[][] W_diff_in_Cost(){
			float h=(float) 0.0001;
			float[][] H;
			int row=W.length;
			int col=W[0].length;
			
			float[][] grad= new float[row][col];
			
			for (int i = 0; i < row*col; i++) {
				float temp_val=W[i%row][i%col];
				W[i%row][i%col]=temp_val+h;
				H = MatMul();
				float costF1=calCost(H);
				W[i%row][i%col]=temp_val-h;
				H = MatMul();
				float costF2=calCost(H);
				W[i%row][i%col]=temp_val;
				grad[i%row][i%col]=(costF1-costF2)/(2*h);
			}
		return grad;
	}
	public static void GradientDescentOptimization(float l) {
		float[][] wdiff=W_diff_in_Cost();
		for (int i = 0; i < W.length; i++) {
			for (int j = 0; j < W[0].length; j++) {
				W[i][j]=W[i][j]-l*wdiff[i][j];
				if(W[i][j]<0)W[i][j]=-W[i][j];
			}
		}
		return;
	}
	public static void minimize(int epoch,float l) {
		int epoch_=epoch;
		while(epoch_-->0) {
			GradientDescentOptimization(l);
		}
		return;
	}
}