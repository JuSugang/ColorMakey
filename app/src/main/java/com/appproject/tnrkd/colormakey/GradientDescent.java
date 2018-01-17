package com.appproject.tnrkd.colormakey;

import com.appproject.tnrkd.colormakey.dto.Color;

import java.util.ArrayList;

public class GradientDescent {
	public ArrayList<Color> X_data=new ArrayList<Color>();
	public float[] Y_data;
	public ArrayList<Float> W;


	public GradientDescent(ArrayList<Color> X_data,float[] Y_data){
		for (int i=0;i<X_data.size();i++) {
			this.X_data.add(X_data.get(i));
		}
		this.Y_data=Y_data;
		int row = this.X_data.size();
		this.W = new ArrayList<Float>();
		for (int i = 0; i < row; i++) {
			this.W.add((float)1);
		}
	}

	public ArrayList<Float> getW(){
		float Wsum=0;
		for (int i = 0; i < W.size(); i++) {
			Wsum+=W.get(i);
		}
		ArrayList<Float> realW=new ArrayList<Float>();
		for (int i = 0; i < W.size(); i++) {
			realW.add(W.get(i)/Wsum);
		}
		return realW;
	}
	public ArrayList<Color> getColor(){
		return X_data;
	}
	public float[] getRGBResult(){
		return MatMul();
	}
	public float getPercent() {
		float[] result=MatMul();
		float rmean=(float)(result[0]+Y_data[0])/2;
		float r=result[0]-Y_data[0];
		float g=result[1]-Y_data[1];
		float b=result[2]-Y_data[2];
		float c=(float)Math.sqrt((2+r/256)*r*r+4*g*g+(2+(255-r)/256)*b*b);
		return (float) Math.round(((764.834-c)*1000/764.834))/10;
	}
	public ArrayList<Float> W_standarize(ArrayList<Float> W){
		ArrayList<Float> edited_W=new ArrayList<Float>();
		float sum=0;
		for (int i = 0; i < W.size(); i++) {
			sum+=W.get(i);
		}
		for (int i = 0; i < W.size(); i++) {
			edited_W.add(W.get(i)/sum);
		}
		return edited_W;
	}
	public float[] MatMul(){
		float[] result= new float[3];
		ArrayList<Float> edited_W= W_standarize(W);
		for (int i = 0; i < 3; i++) {
			float sum=0;
			for (int j = 0; j < X_data.size(); j++) {
				sum+=edited_W.get(j)*(float)(X_data.get(j).mGetRGBarray().get(i));
			}
			result[i]=sum;
		}
		return result;
	}
	public float calCost(float[] H) {
		float result=0;
		for (int i = 0; i < 3; i++) {
			result+=(float)Math.pow(H[i]-Y_data[i], 2)/(float)(H.length);
		}
		return result/2;
	}
	public float[] W_diff_in_Cost(){
		float h=(float) 0.0001;
		float[] H;
		int row=W.size();

		float[] grad= new float[row];

		for (int i = 0; i < row; i++) {
			float temp_val=W.get(i);
			W.set(i,temp_val+h);
			H = MatMul();
			float costF1=calCost(H);
			W.set(i,temp_val-h);
			H = MatMul();
			float costF2=calCost(H);
			W.set(i,temp_val);
			grad[i]=(costF1-costF2)/(2*h);
		}
		return grad;
	}
	public void GradientDescentOptimization(float l) {
		float[] wdiff=W_diff_in_Cost();
		for (int i = 0; i < W.size(); i++) {
			W.set(i, W.get(i)-l*wdiff[i]);
		}
		ArrayList<Float> edited_W= W_standarize(W);
		ArrayList<Integer> removeIndex=new ArrayList<Integer>();
		for (int i = 0; i < wdiff.length; i++) {
			if(edited_W.get(i)<0.05) {
				removeIndex.add(i);
			}
		}
		for (int i = removeIndex.size()-1; i>=0 ; i--) {
			W.remove((int)removeIndex.get(i));
			X_data.remove((int)removeIndex.get(i));
		}

		return;
	}
	public void minimize(int epoch,float l) {
		int epoch_=epoch;
		while(epoch_-->0) {
			GradientDescentOptimization(l);
		}
		return;
	}
}