package problems;

/**
 * How would you reverse the image on an n by n matrix where each pixel is represented by a bit?
 *
 */
public class InverseMatrix {

	public static void inverse(int[][] m) {
		if(m.length>0 && m.length != m[0].length){
			throw new IllegalArgumentException("Matrix must respect the size of nxn");
		}
		
		int size = m.length-1;
		for(int i=0, k=size; i<=k; i++, k--) {
			for(int j=0, l=size; (i<k && j<=size) || (i==k && j<l)  ; j++, l-- ){
				int aux = m[i][j];
				m[i][j] = m[k][l];
				m[k][l] = aux;
			}
		}
	}
}
