import java.awt.*;
import java.applet.*;


public class Funcion
{
    static int P=8;

    public static int binario(int letra,int b[])
    {
	    int i,a=P;
    	int unos=0;	      // introducir en n el codigo ascii y en a un 8
    	while(letra>=2)
    	{
    		b[a]=letra%2;
    		if(b[a]==1)unos++;
    		letra=letra/2;
    		a--;
    	}
    	b[a]=letra;
    	if(letra==1)unos++;
    	a--;
    	for(i=a;i>0;i--) b[i]=0;
    	return unos;
    }

    public static float a_n(float n,int b[])
    {
    	int k;
    	float f;
    	float aux;
    	aux=n*2*(float)Math.PI/P;
    	f=0;
    	for(k=1;k<=P;k++)
    	{
    		 f=f+(float)(b[k]*( Math.cos(k*aux) - Math.cos((k-1)*aux) ));
    	}
    	f=f/((float)Math.PI*n);
    	return (f);
    }


    public static float b_n(float n,int b[])
    {
    	int k;
    	float f;
       	float aux;
    	aux=n*2*(float)Math.PI/P;

    	f=0;
    	for(k=1;k<=P;k++)
    	{
    		f=f+(float)( b[k]*( Math.sin(k*aux) - Math.sin((k-1)*aux) ) );
    	}
    	f=-f/((float)Math.PI*n);
    	return (f);
    }


    public static float suma(float t,float n,float T,float c,int b[],float ampli[])
    {
    	int m;
    	float aux;
    	float f=0,g=0;
    	float sin_cos;
    	float ampli_a=0,ampli_b=0;
    	aux=2*(float)Math.PI*(1/(float)(P*T)) *t;
    	for (m=1;m<=n;m++)
    	{
    		sin_cos=aux*m;
    		ampli_a=a_n(m,b);
    		ampli_b=b_n(m,b);
    		f=f+(float)( ampli_a*Math.sin(sin_cos));
    		g=g+(float)( ampli_b*Math.cos(sin_cos));
    		if(m<16)
    		    ampli[m]=(float)Math.sqrt(Math.pow(ampli_a,2)+(Math.pow(ampli_b,2)));
    	}
    	return(f+g+c);
    }

}
