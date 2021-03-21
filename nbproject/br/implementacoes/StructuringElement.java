package implementacoes;

public class StructuringElement {
    private int[][] kernel;
    private int size;

    public StructuringElement(int size, String type){
        this.size = size;
        this.kernel = generateKernel(type);
    }

    public int[][] getKernel(){
        return this.kernel;
    }

    public int getSize(){
        return this.size;
    }


    private int[][] generateKernel(String type){
        int[][] kernel = new int[this.size][this.size];

        int i, j;

        switch (type) {
            case "CIRCLE":
                int ratio = this.size / 2;

                for(int row = -ratio; row <= ratio; row++ ){
                    for(int column = -ratio; column <= ratio; column++ ){
                        i = row + ratio;
                        j = column + ratio;

                        kernel[i][j] = row*row + column*column <= ratio*ratio ? 1 : 0;
                    }
                }  
                break;
            case "RECT":
                for(int row = 0; row < this.size; row++ ){
                    for(int column = 0; column < this.size; column++ ){
                        kernel[row][column] = 1;
                    }
                }  
                break;
            default:
                break;
        }

        return kernel;
    }

    public String toString(){
        StringBuilder serialized = new StringBuilder();

        serialized.append("[");
        for (int i = 0; i < kernel.length; i++) {
            serialized.append("[");
            for (int j = 0; j < kernel.length; j++) {
                serialized.append(" " + this.kernel[i][j]);
            }
            serialized.append("]\n");
        }
        serialized.append("]\n");

        return serialized.toString();
    }
}
