package implementacoes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;
import swing.janelas.PDI_Lote;

/**
 * Classe para os métodos(funcoes) para o calculo do Greenness, onde serão mantidos
 * , que serao usadas pelo resto do programa.
 * 
 * @author Flavia Mattos & Arthur Costa
 */
public class Greenness {

	/**
	 * Essa função é a implementação da método de GreennesskG = kG − (R + B)
	 * onde K é o valor passado pelo usuário e o R,G e B são os valores obtido da imagem
	 * 
	 * @param img A imagem onde o filtro será aplicado
	 * @param K O valor K da equação
	 * @return retorna a imagem depois de aplicado o filtro
	 */
	public BufferedImage GreennKG(BufferedImage img) {
		BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

		// COME�O NORMALIZA��O
		double min = 0;
		double max = 0;
		int histograma[] = new int[256];
		int histogramaAux[] = new int[256];
		int histograma1[] = new int[256];
		int histograma2[] = new int[256];
		int histogramaEqualizado1[] = new int[256];
		int histogramaEqualizado2[] = new int[256];
		int nivelEqualizado1[] = new int[256];
		int nivelEqualizado2[] = new int[256];
		float novoNivel = 0;
		float soma1 = 0;
		float soma2 = 0;
		float cont = 0;
		float sp = 0;
		float spl = 0;
		float spu = 0;
		float grl1 = 0;
		float grl2 = 0;
		float gru1 = 0;
		float gru2 = 0;
		float dl = 0;
		float du = 0;
		float pll1 = 0;
		float pll2 = 0;
		float plu1 = 0;
		float plu2 = 0;
		float pkl = 0;
		float pku = 0;
		float p1 = 0;
		float p2 = 0;
		
		// Inicializa os vetores dos histogramas
		for (int i=0; i<256; i++) {
			histograma[i] = 0;
			histograma1[i] = 0;
			histograma2[i] = 0;
			histogramaAux[i] = 0;
			nivelEqualizado1[i] = 0;
			nivelEqualizado2[i] = 0;
			histogramaEqualizado1[i]=0;
			histogramaEqualizado2[i]=0;
		}
		
		// Gera o Histograma
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				Color x = new Color(img.getRGB(i, j));
				int cor = x.getGreen();

				histograma[cor]++;
			}
		}
		
		// Calcula a quantidade de pixels
		cont = img.getWidth()*img.getHeight();

		// Exibe o histograma da imagem original
		System.out.println("\nHistograma Original: ");
		for (int i = 0; i<256; i++) {
			System.out.println(histograma[i]);
		}
		

		// Calcula a m�dia SP do histograma da imagem original
		for (int i = 0; i < 256; i++) {
			sp += (histograma[i]/cont) * i;
		}
		System.out.println("\nSP: " +sp);
//		// Exibe a quantidade total de pixels da imagem
//		System.out.println("final: " +cont);
//		
//		// Exibe a m�dia SP
//		System.out.println("SP: " +sp);
//		
		for (int i=0; i<256; i++) {
			if(i<=(int)sp) {
				histograma1[i] = histograma[i];
			}
			else {
				histograma2[i] = histograma[i];
			}
			
			soma1 += histograma1[i];
			soma2 += histograma2[i];
			
		}
		
		for (int i = 0; i < 256; i++) {
			spl += (histograma1[i]/soma1) * i;
		}
		
		for (int i = 0; i < 256; i++) {
			spu += (histograma2[i]/soma2) * i;
		}
		
		grl1 = (sp - spl)/(sp);
		
		System.out.println("\ngrl1: "+grl1);
		
		if (grl1 > 0.5) {
			dl = (1 - grl1)/2;
		}
		else if (grl1 <= 0.5) {
			dl = grl1/2;
		}
		
		grl2 = grl1 + dl;
		System.out.println("\ngrl2: "+grl2);
		
		gru1 = (255 - spu)/(255 - sp);

		System.out.println("\ngru1: "+gru1);
		
		if (gru1 > 0.5) {
			du = (1 - gru1)/2;
		}
		else if (gru1 <= 0.5) {
			du = gru1/2;
		}
		
		gru2 = gru1 + du;
		
		System.out.println("\ngru2: "+gru2);
		
		for(int i=0; i<=(int)sp; i++) {
			if (pkl < histograma1[i]) {
				pkl = histograma1[i];
			}
		}
		
		for(int i=(int)sp+1; i<256; i++) {
			if (pku < histograma2[i]) {
				pku = histograma2[i];
			}
		}
		
		pll1 = grl1 * pkl;
		pll2 = grl2 * pkl;
		plu1 = gru1 * pku;
		plu2 = gru2 * pku;
		
		System.out.println("\npll1: "+pll1);
		System.out.println("\npll2: "+pll2);
		System.out.println("\nplu1: "+plu1);
		System.out.println("\nplu2: "+plu2);
		
		for (int i=0; i<=(int)sp; i++) {
			if (histograma1[i] <= pll2) {
				histograma1[i] = (int) pll1;
			}
			else if (histograma1[i] > pll2) {
				histograma1[i] = (int) pll2;
			}
		}
		
		for (int i=(int)sp+1; i<256; i++) {
			if (histograma2[i] <= plu2) {
				histograma2[i] = (int) plu1;
			}
			else if (histograma2[i] > plu2) {
				histograma2[i] = (int) plu2;
			}
		}
//		for(int i=0; i<=(int)sp; i++) {
//			p1 += (histograma1[i]/(float)soma1);
//		}
		soma1 = 0;
		for(int i=0; i<=(int)sp; i++) {
			soma1 += histograma1[i];
		}
		
		System.out.println("\n\\\\\\\\\\\\\\\\\\\\Novo Nivel 1: ");
		for(int i=0; i<=(int)sp; i++) {
			p1 += (histograma1[i]/(float)soma1);
			novoNivel = (int)(sp * p1);
			nivelEqualizado1[i] = (int) novoNivel;
			histogramaEqualizado1[(int) novoNivel] = histograma1[i];
			
			System.out.println(nivelEqualizado1[i] + "-----" + histogramaEqualizado1[(int) novoNivel]);
		}
		
		novoNivel = 0;
		
//		for(int i=(int)sp+1; i<256; i++) {
//			p2 += (histograma2[i]/(float)soma2);
//		}
		
		soma2 = 0;
		for(int i=(int)sp+1; i<256; i++) {
			soma2 += histograma2[i];
		}
		
		System.out.println("\n\\\\\\\\\\\\\\\\\\\\Novo Nivel 2: ");
		for(int i=(int)sp+1; i<256; i++) {
			p2 += (histograma2[i]/(float)soma2);
			novoNivel = (int) ((sp+1) + (255 - (sp+1)) * p2);
			nivelEqualizado2[i] = (int) novoNivel;
			histogramaEqualizado2[(int)novoNivel] = histograma2[i];
//			System.out.println("p2: " + novoNivel);
			System.out.println(nivelEqualizado2[i] + "-----" + histogramaEqualizado2[(int) novoNivel]);
		}
		
//		for (int i=0; i<256; i++) {
//			if(i<=(int)sp) {
//				histogramaAux[i] = nivelEqualizado1[i];
//			}
//			else {
//				histogramaAux[i] = nivelEqualizado2[i];
//			}
//		}
//		
//		for (int i = 0; i<256; i++) {
//		if (histograma[i] != 0) {
//			min = i;
//			break;
//		}
//	}
	
//	for (int i = 255; i>=0; i--) {
//		if (histogramaAux[i] != 0) {
//			max = i;
//			break;
//		}
//	}
		
		//System.out.println("\n\nHistograma 1: ");
		
//		for(int i1=0; i1<(int)sp; i1++) {
//			System.out.println(histograma1[i1]);
//		}
//		
		//System.out.println("\n\nHistograma 2: ");
		
//		for(int i1=(int)sp+1; i1<256; i1++) {
//			System.out.println(histograma2[i1]);
//		}
		
//		System.out.println("\n\nHistograma Final: ");
//		int somaFinal = 0;
//		for(int i1=0; i1<256; i1++) {
//			System.out.println(histogramaAux[i1]);
//			somaFinal += histogramaAux[i1];
//		}
//		System.out.println("\n\nSoma Final: " + somaFinal);
		//FINAL NORMALIZAÇÃO

		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {

				Color p = new Color(img.getRGB(i, j));
				int atual = p.getGreen();
				int cor = 0;
				int cor1 = 0;
				if (atual <= sp) {
					cor = nivelEqualizado1[atual];
//					if(cor1 != cor) {
//						cor1 = cor;
//						System.out.println(atual + " -----" + cor);
//					}
				}
				else if(atual > sp) {
					cor = nivelEqualizado2[atual];
//					if(cor1 != cor) {
//						cor1 = cor;
//						System.out.println(atual + " -----" + cor);
//					}
				}
				
				//int cor = p.getGreen();
				//int cor1 = histograma[cor];

				Color novo = new Color(cor, cor, cor);
				res.setRGB(i, j, novo.getRGB());
			}
		}
		
//		System.out.println("-----------imagem DIF---------");
//		for (int i = 0; i < img.getWidth(); i++) {
//			for (int j = 0; j < img.getHeight(); j++) {
//				Color x = new Color(img.getRGB(i, j));
//				int cor = x.getGreen();
//				Color y = new Color(res.getRGB(i, j));
//				int cory = y.getGreen();
//				int dif = cor - cory;
//				if(dif != 0) {
//					System.out.println(dif);
//				}
//				
//			}
//		}
		
		return res;
	}
}


