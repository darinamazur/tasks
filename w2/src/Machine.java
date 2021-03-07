import javax.swing.text.Style;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Machine {
    int size_of_A = 0;
    int size_of_S = 0;
    String initial_state;
    int size_of_F;
    ArrayList<String> final_states;
    String[][] Matrix, TempMatrix,NewMatrix;
    ArrayList<String> unproductive = new ArrayList<>();

    void ReadFile() throws IOException {
        File file = new File("C:\\Users\\Дарина\\IdeaProjects\\w2\\src\\file2.txt");
        Scanner scan = new Scanner(file);

        size_of_A = Integer.parseInt(scan.nextLine());
        size_of_S = Integer.parseInt(scan.nextLine());
        initial_state = scan.nextLine();
        size_of_F = Integer.parseInt(scan.nextLine());
        Matrix = new String[size_of_A+1][size_of_S+1];
        TempMatrix = new String[size_of_A+1][size_of_S+1];
        for(int i = 0; i < size_of_A + 1; i++){
            for(int j = 0; j < size_of_S + 1; j++){
                Matrix[i][j] = "-";
            }
        }
        for(int i = 1; i < size_of_S + 1; i++){
            Matrix[0][i] = String.valueOf(i - 1);
        }
        for(int j = 1; j < size_of_A + 1; j++){
            Matrix[j][0] = Character.toString('a'+ j - 1);
        }

        String fileContent = "";
        fileContent = scan.nextLine();
        final_states= new ArrayList<String>(Arrays.asList(fileContent.split("\\s+")));
        String[] to_matrix;
        while (scan.hasNextLine()) {
            fileContent = scan.nextLine();
            to_matrix = fileContent.split("\\s+");
            int k,p;
            if(to_matrix.length == 3) {
                k = (int) to_matrix[1].charAt(0) - (int) ('a') + 1;
                p = Integer.parseInt(to_matrix[0]) + 1;
                Matrix[k][p] = to_matrix[2];
            }
        }
        System.out.println("Matrix: ");
        for(int i = 0; i < size_of_A + 1; i++){
            for(int j = 0; j < size_of_S + 1; j++){
                System.out.print(Matrix[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println("Final states: ");
        for(int i = 0; i<final_states.size();i++){
            System.out.print(final_states.get(i) + " ");
        }
        System.out.println("\nInitial state: ");
        System.out.println(initial_state);
        GraphViz(Matrix,size_of_A+1,size_of_S+1,final_states,initial_state,1, unproductive);
    }

    void ClassEquals( ArrayList<ArrayList<String>> columns, ArrayList<HashSet<String>> first){
        HashSet<String> temp = new HashSet<>();
        HashSet<Integer> odd = new HashSet<>();
        for(int i = 0; i < columns.size(); i ++){
            if(odd.contains(i) == false)
                temp.add(Integer.toString(i));
            for(int j = i + 1; j < columns.size(); j++) {
                if (columns.get(i).equals(columns.get(j)) == true) {
                    if(odd.contains(j) == false){
                        temp.add(Integer.toString(j));
                        odd.add(j);
                    }
                }
            }
            if(temp.size() != 0)
                first.add(temp);
            temp = new HashSet<>();
        }
    }

    void FindColumns(ArrayList<String> column, ArrayList<ArrayList<String>> columns){
        for(int i = 1; i < size_of_S + 1; i++){
            for(int j = 1; j < size_of_A + 1; j++) {
                column.add(TempMatrix[j][i]);
            }
            columns.add(column);
            column = new ArrayList<>();
        }
    }

    void FillInitialSets(HashSet<String> pi1,HashSet<String> pi2){
        for(int i = 0; i < final_states.size(); i++)
            pi1.add(final_states.get(i));

        for(int j = 0; j < size_of_S; j ++)
            if(pi1.contains(Integer.toString(j)) == false){
                pi2.add(Integer.toString(j));
            }

        for(int i = 1; i < size_of_A + 1; i++){
            for(int j = 1; j < size_of_S + 1; j++){
                if(pi1.contains(Matrix[i][j]) == true && Matrix[i][j] != "-")
                    TempMatrix[i][j] = "pi1";
                else
                    if(Matrix[i][j] != "-")TempMatrix[i][j] = "pi2";
            }
        }

    }

    void ChangeMatrixByEq(ArrayList<HashSet<String>> set){
        for(int i = 1; i < size_of_A + 1; i++){
            for(int j = 1; j < size_of_S + 1; j++){
                    for(int k = 0; k < set.size(); k++) {
                        if (set.get(k).contains(Matrix[i][j]) && Matrix[i][j] != "-") {
                            TempMatrix[i][j] = "f" + k;
                        }
                    }
            }
        }
    }
    void Copy(){
        for(int i = 0; i < size_of_A + 1; i++){
            for(int j = 0; j < size_of_S + 1; j++){
                TempMatrix[i][j] = Matrix[i][j];
            }
        }
    }

    void CreateNewMatrix(ArrayList<HashSet<String>> second) throws IOException {
        int length = second.size();
        NewMatrix = new String[size_of_A+1][length + 1];

        for(int i = 0; i < size_of_A + 1; i++){
            for(int j = 0; j < length + 1; j++){
                NewMatrix[i][j] = "-";
            }
        }
        for(int i = 1;i < size_of_A + 1; i++)
            NewMatrix[i][0] = Matrix[i][0];

        for(int j = 1; j < length + 1; j++)
            NewMatrix[0][j] = Integer.toString(j - 1) + "1";
        for(int i = 1; i < size_of_A + 1; i++){
            for(int j = 1; j < length + 1; j++){
                for(int k = 0; k < length; k++){
                    if(second.get(k).contains(Matrix[i][Integer.parseInt(second.get(j-1).iterator().next())+1])){
                        NewMatrix[i][j] = second.get(k).iterator().next()+"1";
                    }
                }
            }
        }
        ArrayList<String> new_final_states = new ArrayList<>();
        for(int i = 0; i < length; i++){
            for(int j = 0; j < final_states.size(); j++){
                if(second.get(i).contains(initial_state))
                    initial_state = second.get(i).iterator().next() + "1";
                if(second.get(i).contains(final_states.get(j))){
                    if(new_final_states.contains(second.get(i).iterator().next() + "1") == false)
                    new_final_states.add(second.get(i).iterator().next() + "1");
                }
            }
        }
        System.out.println("New final states: " + new_final_states);
        System.out.println("New initial state: " + initial_state);
        unproductive = new ArrayList<>();
        Analysis(size_of_A + 1,length+1,new_final_states);
        if(unproductive.isEmpty())
        GraphViz(NewMatrix,size_of_A + 1,length+1,new_final_states,initial_state,2,unproductive);
        else{
            System.out.println("Unproductive states: " + unproductive);
            for(int k = 0; k < unproductive.size(); k ++){
                for(int i = 0; i < size_of_A + 1; i++){
                    for(int j = 0; j < length + 1; j++){
                        if(NewMatrix[i][j] == unproductive.get(k))
                            NewMatrix[i][j] = "-";
                    }
                }
            }
            GraphViz(NewMatrix,size_of_A + 1,length+1,new_final_states,initial_state,2,unproductive);
        }

    }
    void Analysis(int n, int m, ArrayList<String> finals){
        ArrayList<String> in = new ArrayList<>(), out = new ArrayList<>();
        for(int k = 1; k < m; k++){
            boolean flag = true;
            for(int i = 1; i < n; i++){
                for(int j = 1; j < m; j++){
                    if(NewMatrix[i][j].equals(NewMatrix[0][k])){
                         flag = false; break;
                    }
                }
                if(!flag) break;
            }
            if(flag && !in.contains(NewMatrix[0][k]))
                in.add(NewMatrix[0][k]);
            //System.out.println("in" + in);

            boolean flag1 = true;
            for(int l = 1; l < n; l++){
                if(!NewMatrix[l][k].equals("-")){
                    flag1 = false; break;
                }

            }
                if(flag1)
                out.add(NewMatrix[0][k]);
            //System.out.println("out" + out);
        }
       for(int i = 1; i < m; i++){
            if(in.contains(NewMatrix[0][i])) unproductive.add(NewMatrix[0][i]);
            else
                if(out.contains(NewMatrix[0][i]) && !finals.contains(NewMatrix[0][i]))
                    unproductive.add(NewMatrix[0][i]);
        }

    }
    void split() throws IOException {
        HashSet<String> pi1 = new HashSet<>(), pi2 = new HashSet<>();
        Copy();
        FillInitialSets(pi1, pi2);

        ArrayList<String> column = new ArrayList<>();
        ArrayList<ArrayList<String>> columns = new ArrayList<>();
        ArrayList<HashSet<String>> first = new ArrayList<>(), second = new ArrayList<>();

        FindColumns(column, columns);
        ClassEquals(columns, first);
        ChangeMatrixByEq(first);
        //System.out.println("first" + first);
        while(true) {
            column = new ArrayList<>(); columns = new ArrayList<>();
            FindColumns(column, columns);
            ClassEquals(columns, second);
            ChangeMatrixByEq(second);
            //System.out.println("second" + first);
            if(first.equals(second)) break;
            else {
                ArrayList<HashSet<String>> temp = second;
                //System.out.println("temp" + temp);
                first = temp;
                //System.out.println("first" + first);
                second = new ArrayList<>();
            }
        }

        System.out.println("Infinity eq " + second);
        CreateNewMatrix(second);


        /*System.out.println("Matrix: ");
        for(int i = 0; i < size_of_A + 1; i++){
            for(int j = 0; j < size_of_S + 1; j++){
                System.out.print(TempMatrix[i][j] + "   ");
            }
            System.out.println();
        }*/
        System.out.println("Final Matrix: ");
        for(int i = 0; i < size_of_A + 1; i++) {
            for (int j = 0; j < second.size() + 1; j++) {
                System.out.print(NewMatrix[i][j] + "    ");
            }
            System.out.println();
        }
    }


    void GraphViz(String[][] M, int n, int m, ArrayList<String> finals, String initial, int index, ArrayList<String> un) throws IOException {
        FileWriter writer = new FileWriter("C:\\Users\\Дарина\\IdeaProjects\\w2\\src\\graph"+ index +".txt");

            writer.write("digraph G {\n");
            for(int i = 1; i < n; i++){
                if(!M[i][0].equals("-"))
                for(int j = 1; j < m; j++){
                    if(!M[i][j].equals("-") && !M[0][j].equals("-"))
                    writer.write(M[0][j] + "->" + M[i][j] +"[label ="+ M[i][0] +"]\n");
                }
            }
            for(int j = 1; j < m; j++){
                if(!M[0][j].equals("-"))
                writer.write(M[0][j] +"\n");
            }
            for(int k = 0; k < finals.size(); k++){
                if(!un.contains(finals.get(k)))
                writer.write(finals.get(k) + "[style=filled,shape=\"doublecircle\",fillcolor=\"antiquewhite:aquamarine\"]\n");
            }
            if(!un.contains(initial))
            writer.write(initial + "[style=filled,shape=\"rarrow\",fillcolor=\"yellow\"]\n");
            writer.write("}");
        writer.close();
    }
}



