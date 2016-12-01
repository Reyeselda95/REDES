import Funcion;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

// Funcion de inicializacion
public class Redes extends java.applet.Applet
{
    public void init()
    {
        RedesContrEntrada controles;	// Mandos de entrada
        RedesContrModo contrModo;		// Seleccion de modos

        setLayout(new BorderLayout());  // Crea los bordes
    	Solucion c = new Solucion();	// Crea el canvas
        add("Center", c);				// Añade en el centro el canvas

    	// Anade controles de entrada y controles de modo
		add("South", controles = new RedesContrEntrada(c)); 
        add("North", contrModo = new RedesContrModo(c));
    }
}

class RedesContrEntrada extends Panel  // clase que crea los  botones
    implements ActionListener
{
    Solucion canvas;
    TextField a;
	static Label nvLabel;
    static TextField nv;
	TextField l;
    static TextField r;
    Button DibuButton;

    public RedesContrEntrada(Solucion canvas) // funcion que crea los botones
    {
        Component et;          //this.canvas : var. global de la clase (10 lineas mas arriba)
    	this.canvas = canvas; // para copiar el canvas(var global) y poder repintarlo en action
    	//resize(640,480);  // le damos tamaño a la ventana

        nvLabel = new Label("Hola");    	
		nv=new TextField("    ");
		if (Solucion.bModoArmVelo)
		{
			nv.setText("" + canvas.n);
			nvLabel.setText("Arm.->");
		}
		else
		{
			nv.setText("" + canvas.vel); 
			nvLabel.setText("Vel.->");
		}
		add(nvLabel);
    	add(nv);
        
		et = new Label( "A.Banda (Hz)->" );
    	add(et);  // creamos la etiqueta y la guardamos
		a=new TextField("     ");
		a.setText("" + canvas.ancho);    	
    	add(a); // campo para meter valores, al cual le damos valores por defecto

        et = new Label( "S/N (dB)->" );
    	add(et);  // creamos otra etiqueta y le damos valores
    	r=new TextField("    ");
		r.setText("" + canvas.ruido);    	
		add(r);
        
		et = new Label( "Car.->" );
    	add(et);
    	l=new TextField("  ");
		l.setText("" + (char)canvas.letra);
		add(l); // meto la letra  b por defecto

		// Para separar un poco el boton
		add(new Label("  "));

    	DibuButton=new Button("Pinta");
        DibuButton.addActionListener(this);


    	add(DibuButton);  //  creo el boton de dibujar
		//DibuButton.reshape(252,8,40,26);
    }

	// funcion que se ejecuta cuando se produce un evento, ejemplo pulsar una tecla        
    public void actionPerformed(ActionEvent ev)
    {
        char aux[];
        //aux=new char [10];

		// Si se ha pulsado un boton (el de dibujar) o la tecla de retorno
    	if (ev.getSource() instanceof Button) 
    	{
            // Si hay que leer armonicos
			if (canvas.bModoArmVelo)
			{
				// Lee numero de armonicos de la caja de texto
				canvas.n = Integer.parseInt(nv.getText().trim()); 
				if (canvas.n <= 0)
				{
					canvas.n = 1;
					nv.setText("" + canvas.n);
				}
				// Asegura un valor correcto
				else if (canvas.n >= canvas.NArmMax)
				{
					canvas.n = canvas.NArmMax;
					nv.setText("" + canvas.n);
				}
			}
			else
			{				
				// Lee velocidad de transmision de la caja de texto
				canvas.vel = Integer.parseInt(nv.getText().trim());
				// Asegura un valor correcto
				if (canvas.vel < 5)
				{
					canvas.vel = 5;
					nv.setText("" + canvas.vel);
				}
			}
			
			// Lee ancho de banda de la caja de texto
            canvas.ancho = Integer.parseInt(a.getText().trim());
			// Asegura un valor correcto
			if (canvas.ancho < 0)
			{
				canvas.ancho = 0;
				a.setText("" + canvas.ancho);
			}
		
			// Lee valor de relacion señal ruido de la caja de texto
            canvas.ruido = Integer.parseInt(r.getText().trim());
			// Asegura un valor correcto
			if (canvas.ruido >= 60)
			{
				canvas.ruido = 60;
				r.setText("" + canvas.ruido);
			}
			else if (canvas.ruido < -10)
			{
				canvas.ruido = -10;
				r.setText("" + canvas.ruido);
			}

            // Lee caracter de la caja de texto y lo mete en un array
			aux=(l.getText().trim()).toCharArray(); 
			// Obtiene caracter
            canvas.letra=(int)(aux[0]);

            // Llama a canvas para redibujar la señal
			canvas.repaint(); 

       }
    }
}

class RedesContrModo extends Panel  // clase que crea los  botones
    implements ItemListener
{
    Solucion canvas;
	Checkbox ckbArmVelo;
    Checkbox ckbVeloArm;
    CheckboxGroup ckbgModoOper;
	Checkbox ckbRuido;
    Checkbox ckbSinRuido;
    CheckboxGroup ckbgModoRuido;

    public RedesContrModo(Solucion canvas) // funcion que crea los botones
    {
        Component et;          //this.canvas : var. global de la clase (10 lineas mas arriba)
    	this.canvas = canvas; // para copiar el canvas(var global) y poder repintarlo en action
		//resize(640,480);  // le damos tamaño a la ventana

        // Checkbox para escoger el modo
        ckbgModoOper = new CheckboxGroup();

        ckbArmVelo = new Checkbox("Armonicos -> Velocidad", ckbgModoOper, canvas.bModoArmVelo);
        ckbArmVelo.addItemListener(this);
        add(ckbArmVelo);
        ckbVeloArm = new Checkbox("Velocidad -> Armonicos", ckbgModoOper, !canvas.bModoArmVelo);
        ckbVeloArm.addItemListener(this);
        add(ckbVeloArm);

        // Para separar los grupos de checkboxes
		add(new Label("         "));

        // Checkbox para escoger ruido-sin ruido
        ckbgModoRuido = new CheckboxGroup();

        ckbRuido = new Checkbox("Con ruido", ckbgModoRuido, canvas.bModoRuido);
        ckbRuido.addItemListener(this);
        add(ckbRuido);
        ckbSinRuido = new Checkbox("Sin ruido", ckbgModoRuido, !canvas.bModoRuido);
        ckbSinRuido.addItemListener(this);
        add(ckbSinRuido);
    }

    public void itemStateChanged(ItemEvent ev) 
    {
        // funcion que se ejecuta cuando se produce un evento, ejemplo pulsar una tecla

        if (ev.getSource() == ckbArmVelo)
			canvas.bModoArmVelo = true;
		else if (ev.getSource() == ckbVeloArm)
			canvas.bModoArmVelo = false;
		else if (ev.getSource() == ckbRuido)
			canvas.bModoRuido = true;
		else if (ev.getSource() == ckbSinRuido)
			canvas.bModoRuido = false;

		canvas.repaint();  // llamamos a canvas redibujando la señal
    }
}

class Solucion extends java.awt.Canvas // Canvas son funciones para pintar
{
	// Estas variables se hacen estaticas para que aunque se salga
	// fuera del objeto sigamos teniendo los valores

    static int P = 8;					// Numero de bits
    static int vel = 600;				// Velocidad transmision
	static int ancho = 1000;			// Ancho de banda
	static int letra = 'b';				// Carcater a transmitir
	static int ruido = 60;				// Relacion señal ruido
	static int n = 8;					// Numero de armonicos
    static boolean bModoArmVelo = false;// Modo de operacion
    static boolean bModoRuido = false;	// Activar o no el ruido
	
	static final int NArmMax = 120;		// Numero maximo de armonicos 
										// para calculos
    
	final float T = 40;					// Duracion de un bit (en pixels)

	// Funcion que pinta es llamada por repaint
    public void paint(Graphics g)
    {
        Repre(g);
    }

	// Dibuja toda la pantalla representando la funcion
    void Repre(Graphics g)
    {
        double v,aux,dif;
        int t,n_unos,cero,uno;
        int vmax;
        int alto,i,j;
        int posx,posy,despla;
        int alto_fuente,ancho_fuente;
        float c;
		double ruidoSN=0.0;
        int b[];
        float ampli[];
        FontMetrics fuente;
        char msg[];
        String cadena;
		int nmax, num_ar;
		int yL1, yL0;
		int centroBit;
			
		// Inicia vectores
		msg = new char [P + 4];	// Para escribir valores
        ampli = new float [17];	// Valores de armonicos a mostrar
		b = new int [P + 2];	// Balores binarios de la señal digital
				
		// Si hay ruido calcula velocidad maxima segun ese ruido
		if (bModoRuido)
        {
			// permite editar el control de  ruido
			RedesContrEntrada.r.setEditable(true);

			// Calcula relacion señal ruido en base 10
			ruidoSN = Math.pow(10,(double)((ruido>0)? ruido:0)/10);
            // Velocidad maxima segun Shanon
			// [ log2(n) = log10(n) / log10(2) ]
                        vmax = (int) (ancho*Math.log(1+ruidoSN)/Math.log(2));                                              
		}
        // Si no hay ruido, la velocidad es maxima
        else
        {
			// No permite editar el control de entrada de ruido
			RedesContrEntrada.r.setEditable(false);
            
			// Velocidad maxima
			vmax = 2 * ancho;			
		}
		
		// Si el modo es armonicos->velocidad
		if (bModoArmVelo)
		{
			// Asigna velocidad maxima
                        if (n > 0)
                                vel = ancho * P / n;
                        else
                                vel = 0;

			// Actualiza campo de entrada de armonicos y velocidad
			RedesContrEntrada.nvLabel.setText("Arm.->");
			RedesContrEntrada.nv.setText(""+n);			
		}
		// Si el modo es velocidad->armonicos
		else
		{
			// calcula numero armonicos evitando dividir por 0
			if (vel > 0)
				n = ancho * P / vel;
			else 
				n = 0;

			// Actualiza campo de entrada de armonicos y velocidad
			RedesContrEntrada.nvLabel.setText("Vel.->");
			RedesContrEntrada.nv.setText(""+vel);			
		}

		// Valores de referencia para pintar graficas
        posy = 70;
        posx = 3;
        alto = 40;
		
		// Fondo y tamaño del applet
		g.setColor(Color.black);
        g.fillRect(0,0,getSize().width,getSize().height);

		// Otiene medidas de la fuente        
        fuente = g.getFontMetrics();
        ancho_fuente = fuente.charWidth('0');
        alto_fuente = fuente.getHeight();

		// Pinta titulo  en amarillo
		g.setColor(Color.yellow);
		g.drawString("Práctica 2 de R.C.   TRANSMISION DE SEÑALES", 170,alto_fuente + 2);
		
        // Pinto los 0 y 1 de los ejes Y de en rojo
		g.setColor(Color.red);
		g.drawString("0",posx+1,posy+2);  
        g.drawString("1",posx+1,posy-alto);
        g.drawString("0",posx+1,posy+100);
        g.drawString("1",posx+1,posy+45+alto_fuente);
        
		// Pinta letras de los ejes en verde
		g.setColor(Color.green);  
        g.drawString("V(logico)",posx+ancho_fuente,posy-alto-alto_fuente);
        g.drawString("T",(int)(ancho_fuente+10+posx+P*T),posy-2+(alto_fuente/2));
        g.drawString("V",posx+ancho_fuente,posy+45);
        g.drawString("T",(int)(ancho_fuente+10+posx+P*T),posy+96+(alto_fuente/2));
		
		// Dibuja ejes de color azul
        posx += ancho_fuente + 3;
        g.setColor(Color.blue);  
        g.drawLine(posx,posy+2,posx,posy-alto-10);
        g.drawLine(posx,posy+2,(int)(posx+P*T+1),posy+2);
        g.drawLine(posx,posy+100,posx,posy+50);
        g.drawLine(posx,posy+100,(int)(posx+P*T+1),posy+100);
        
		// Convierte caracter en una señal binaria de 8 bits
		// y obtiene numero de unos de esa señal
		n_unos = Funcion.binario(letra,b);

		// Pinta 1 y 0 de la señal binaria en eje X en blanco		
        g.setColor(Color.white);
        for (i=1; i<=8; i++)
            g.drawString(""+b[i],posx+20+((i-1)*40),posy+19);

		// Dibuja señal binaria en color rojo
        g.setColor(Color.red);
    	posx++;
    	for (t=0; t<P; t++)
    	{
			// Pinta las barras horizontales
    	    g.drawLine((int)(posx+(t*T)),posy-b[t+1]*alto,(int)(posx+(t+1)*T),posy-b[t+1]*alto);
    	    // Pinta las barras verticales
			if(b[t+1]!=b[t+2])
    	        g.drawLine((int)(posx+(t+1)*T),posy-alto,(int)(posx+(t+1)*T),posy);
    	}

		// Calcula valor constante para la suma de Fourier
	    c = (float)n_unos/P/2;
	    		
        // Obtiene numero de armonicos a considerar en el calculo
		nmax = n;
		if(nmax > NArmMax) 
			nmax = NArmMax;
        
		// Calcula delimitadores del ruido
		despla = 26;
		posy += 100;		
		if (bModoRuido)
		{
			i = despla + 2;
			yL1 = yL0 = posy - i;
			i = (int)(i / ruidoSN);			
			yL1 -= i;
			yL0 += i;
		}
		else
		{
			yL1 = yL0 = posy - despla;
		}
		
		// Dibuja delimitadores del ruido en azul claro
		g.setColor(Color.cyan);
		g.drawLine(posx, yL1,(int)(posx+P*T+1), yL1);
		g.drawLine(posx, yL0,(int)(posx+P*T+1), yL0);

		// Inicia funcion, partiendo de una señal que enpieza
		// con un escalon, para calcular desplazamiento
		cero = b[1];
        uno = b[2];
        b[1] = 0;
        b[2] = 1;
        v = Funcion.suma(40,nmax,T,c,b,ampli);
        b[1] = cero;
        b[2] = uno;

        // Calcula el desplazamiento de la señal respecto a sus ejes
		dif = v * 50 + despla;
        		
        // Calcula y dibuja la señal reconstruida en rojo
		g.setColor(Color.red);
		v = Funcion.suma(0,nmax,T,c,b,ampli);
		centroBit = (int)(T/2);
		i = 1;
		for (t=1; t<=(P*T); t++)
    	{
    		// calcula siguiente tramo
			aux = v;
    		v = Funcion.suma(t,nmax,T,c,b,ampli);
			
			// dibuja tramo			
    		g.drawLine((t-1)+posx, (int)((aux*50)+posy-dif),
				t+posx, (int)((v*50)+posy-dif));
			
			// calcula el valor digital del centro del bit
			if (t >= centroBit)
			{
				j = (int)((v*50)+posy-dif);
				if (j < yL1)
					msg[i] = '1';
				else if (j > yL0)
					msg[i] = '0';
				else
					msg[i] = '?';
				centroBit += (int)T;
				i++;
			}
    	}

		// Pinta valores de bits (0s y 1s) de la señal reconstruida
		g.setColor(Color.white);
		for (i=1; i<=8; i++)
            g.drawString(""+msg[i],posx+20+((i-1)*40),posy+19);

		// Número de armónicos a dibujar, como maximo 14
        num_ar = n; 
        if(num_ar > 14) 
			num_ar=14;
        
		// Si hay armonicos a dibujar...
		posy += 110;
        if (num_ar > 0)
        {
			// Pinta lineas de los ejes de los armonicos    		
    		g.setColor(Color.blue); 
                g.drawLine(posx,posy,posx,posy-65);
                g.drawLine(posx,posy,(int)(posx+num_ar*T),posy);

			// Pinta letras de los ejes de armonicos
			g.setColor(Color.green);
    		g.drawString("A",posx-2,posy-70);
    		g.drawString("N",(int)(posx+num_ar*T+8),posy+4);			
        }

		// Pinta números de los armónicos
		g.setColor(Color.white);
    	for (j=1; j<=num_ar; j++)
	    g.drawString(""+j,(int)((j)*40-6-fuente.stringWidth(""+j)/2),posy+14);
		
		// Pinta las barritas de los armonicos
        posx -= 20;
        g.setColor(Color.magenta);
        for (i=1;i<=num_ar;i++)
        {
			// Solo se pinta el armonico si su valor es suficiente
			if (ampli[i]>0.00999)
				for (j=0;j<5;j++)
					g.drawLine(posx+(40*i)+j-2,posy-2,posx+(40*i)+j-2,(int)(posy-(ampli[i]*100)));
        }

	// Calcula componente continua
	i=0;
        for (j=1; j<=8; j++)
        {
            if (b[j]==1)
                i+=1;
	}
        i*=7;
        // Solo se pinta componente continua si su valor es suficiente
	if (i>0)
        {
            g.setColor(Color.magenta);
            for (j=0;j<5;j++)
  	        g.drawLine(posx+20+j-2,posy-2,posx+20+j-2,posy-i);
        }
        g.setColor(Color.white);
        g.drawString("cc", posx+14, posy + 13);

		// Pinta valores de los armonicos encima de las barras
        g.setColor(Color.white);
        for (j=1;j<=num_ar;j++)
        {
			// Si es un valor muy pequeño, pintara un 0.000
            if (ampli[j]<0.009999999)
            {
                msg[0] = '0'; msg[1] = '.';	msg[2] = '0'; msg[3] = '0'; msg[4] = '\0';
            }
            else  // sino, pintara los tres primeros digitos
            {
                cadena = "" + ampli[j];
                cadena.getChars(0,4,msg,0);
                msg[4] = '\0';
            }
			// Pinta valor
            g.drawChars(msg,0,4,(int)((j)*40+30-fuente.stringWidth(""+msg)/2),(int)(posy-(ampli[j]*100)-5));
        }

		// Pinta copyright en verde
		g.setColor(Color.cyan);
		g.drawString("Fourier v2.0. ©1999. D.F.I.S.T.S. Universidad de Alicante.", 5, posy + 32);
		// Pinta referencias de los ejes
        g.setColor(Color.white);
        posx = 0;
        posy = 40;
        g.drawString("V = AMPLITUD EN VOLTIOS",(int)(posx+P*T+75),posy);
        g.drawString("T = TIEMPO EN SEGUNDOS",(int)(posx+P*T+75),posy+alto_fuente);
        g.drawString("N = NUMERO DE ARMONICO",(int)(posx+P*T+75),posy+alto_fuente*2);
        g.drawString("A = POTENCIA DE ARMONICOS",(int)(posx+P*T+75),posy+alto_fuente*3);

		// Pinta informacion general
        posy += alto_fuente * 5;
        posx += P * T + 75;

        g.setColor(Color.magenta);
        g.drawString("INFORMACION:",posx,posy);
        posy+= 20;

        i=0;
        j=posx+ancho_fuente*22;

        g.setColor(Color.red);
        g.drawString("Ancho de banda:",posx,posy+alto_fuente*i);
        g.setColor(Color.green);
        g.drawString(""+ancho+" Hz",j,posy+alto_fuente*i);
        g.setColor(Color.red);
		i++;

        g.setColor(Color.red);
        g.drawString("Carácter transmitido:",posx,posy+alto_fuente*i);
        g.setColor(Color.green);
        g.drawString(""+(char)letra,j,posy+alto_fuente*i); 
		i++;

        g.setColor(Color.red);
        if (bModoRuido)
        {
            g.drawString("Relación señal/ruido:",posx,posy+alto_fuente*i);
            g.setColor(Color.green);
            g.drawString(""+ruido+" dBs",j,posy+alto_fuente*i);
        }
		else
        {
            g.drawString("Relación señal/ruido:",posx,posy+alto_fuente*i);
            g.setColor(Color.green);
            g.drawString("sin ruido",j,posy+alto_fuente*i);             
        }
        i+=2;

		// Pinta resultados
        g.setColor(Color.magenta);
        g.drawString("RESULTADOS:",posx,posy+alto_fuente*i);
        i+=2;

        g.setColor(Color.red);
		if (bModoArmVelo)
        {
            g.drawString("Velocidad transmision:",posx,posy+alto_fuente*i);
            g.setColor(Color.green);
            g.drawString(""+ vel +" bps",j,posy+alto_fuente*i);
        }
		else
        {
            g.drawString("Número de armónicos:",posx,posy+alto_fuente*i);
            g.setColor(Color.green);
            g.drawString(""+n,j,posy+alto_fuente*i);
        }
		i++;

        g.setColor(Color.red);
        if (bModoRuido)
            g.drawString("Velocidad T.Shannon:",posx,posy+alto_fuente*i);
        else
            g.drawString("Velocidad T.Nyquist:",posx,posy+alto_fuente*i);        
		g.setColor(Color.green);
        g.drawString("" + vmax +" bps",j,posy+alto_fuente*i);
    }
}

// Fin
