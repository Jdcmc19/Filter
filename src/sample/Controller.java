package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    Label lblTotalTabla1,lblTotalTabla2;
    @FXML
    Button btoListo, btoListoedit;

    @FXML
    CheckBox checkF0pares, checkF1impares, checkF2consecutivos3, checkF3consecutivos5,checkF4restarTablas;

    @FXML
    CheckBox num0,num1,num2,num3,num4,num5,num6,num7,num8,num9,num10,num11,num12,num13,num14,num15,num16,num17,num18,num19,num20,num21,num22,num23,num24,num25,num26,num27,num28,num29,num30,num31,num32,num33,num34,num35,num36;

    @FXML
    Pane pane;

    @FXML
    private TableView<Combinacion> tableCombinaciones;
    @FXML
    private TableColumn<Combinacion,Integer> col0, col1, col2, col3, col4;
    @FXML
    private TableView<Combinacion> tableCombinacionesedit;
    @FXML
    private TableColumn<Combinacion,Integer> col0edit, col1edit, col2edit, col3edit, col4edit;

    @FXML
    Button btoEliminarCombinacion, btoRehacer, btoGuardarTabla1,btoFiltrar,btoAgregar,btoCargarTabla1;
    @FXML
    Button btoEliminaredit, btoRehaceredit, btoCargarTabla2, btoGuardarTabla2,btoFiltraredit,btoSelectTodos,btoSelectNinguno;
    @FXML
    TextField txtNombreArchivo, txtFieldNuevoNumero;

    File fileCombinaciones = null;
    ArrayList<Combinacion> tmp = new ArrayList<>();
    ArrayList<Integer> itemsHash = new ArrayList<>();
    ObservableList<Combinacion> items = FXCollections.observableArrayList(tmp);
    ObservableList<Combinacion> itemsTabla2;
    ArrayList<Combinacion> ultimaBorrada = new ArrayList<>();
    ArrayList<Combinacion>ultimaBorradaedit = new ArrayList<>();
    ArrayList<Combinacion> al;
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    public void initialize(URL fxmlLocations, ResourceBundle resources){
        alert.setTitle("Cargando...");
        alert.setHeaderText("Leyendo o escribiendo informacion.");
        alert.setContentText("Por favor espere.");

        btoCargarTabla2.setOnAction(event -> {
            fileCombinaciones = getFile();
            ArrayList<Combinacion> pba = new ArrayList<>();
            if(fileCombinaciones!=null){
                Thread thread = new Thread(() -> {
                    Platform.runLater(() -> {
                        alert.showAndWait();
                    });
                    setDataTable2(readExcelFileFranc(fileCombinaciones,pba));
                    Platform.runLater(() -> {
                        alert.hide();
                    });
                });
                thread.start();
            }
        });
        btoCargarTabla1.setOnAction(event -> {
            fileCombinaciones = getFile();
            ArrayList<Combinacion> pba = new ArrayList<>();
            itemsHash = new ArrayList<>();
            if(fileCombinaciones!=null){
                Thread thread = new Thread(() -> {
                    Platform.runLater(() -> {
                        alert.showAndWait();
                    });
                    setDataTable1(readExcelFileFranc(fileCombinaciones,pba));
                    for(int i = 0;i<items.size();i++){
                        itemsHash.add(items.get(i).hash());
                    }
                    Platform.runLater(() -> {
                        alert.hide();
                    });
                });
                thread.start();
            }
        });
        btoFiltrar.setOnAction(event -> {

            al = new ArrayList<>(items);
            Thread thread = new Thread(() -> {
                Platform.runLater(() -> {
                    alert.showAndWait();
                });
                if(checkF4restarTablas.isSelected()){
                    al = restarLista();
                }
                for(int i=0;i<items.size();i++){
                    Combinacion cc = items.get(i);
                    if((checkF0pares.isSelected()&&cc.pares())||(checkF1impares.isSelected()&&cc.impares())||(checkF2consecutivos3.isSelected()&&cc.consecutivos())||(checkF3consecutivos5.isSelected()&&cc.consecutivos2())){
                        al.remove(cc);
                        itemsHash.remove((Integer)cc.hash());
                    }
                }
                setDataTable1(al);
                resetChecks();
                Platform.runLater(() -> {
                    alert.hide();
                });
            });
            thread.start();
        });
        btoFiltraredit.setOnAction(event -> {
            Thread thread = new Thread(() -> {
                Platform.runLater(() -> {
                    alert.showAndWait();
                });
                ArrayList<Combinacion> al = new ArrayList<>(itemsTabla2);
                for(int i=0;i<itemsTabla2.size();i++){
                    Combinacion cc = itemsTabla2.get(i);
                    if((checkF0pares.isSelected()&&cc.pares())||(checkF1impares.isSelected()&&cc.impares())||(checkF2consecutivos3.isSelected()&&cc.consecutivos())||(checkF3consecutivos5.isSelected()&&cc.consecutivos2())){
                        al.remove(cc);
                    }
                }
                setDataTable2(al);
                resetChecks();
                Platform.runLater(() -> {
                    alert.hide();
                });
            });
        thread.start();
        });
        btoGuardarTabla1.setOnAction(event -> {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Seleccione donde guardar el archivo.");
            File tmp = dc.showDialog(null);
            String nuevoArchivo="";
            if(tmp!=null)
                nuevoArchivo = tmp.getAbsolutePath();
            if(nuevoArchivo!="")
                nuevoArchivo+='\\'+txtNombreArchivo.getText()+".xlsx";
            fileCombinaciones = new File(nuevoArchivo);
            try{
                fileCombinaciones.createNewFile();
            }catch (IOException ioe){}
            Thread thread = new Thread(() -> {
                Platform.runLater(() -> {
                    alert.showAndWait();
                });
                writeExcelFile(fileCombinaciones,items);
                txtNombreArchivo.setText("Combinaciones");
            Platform.runLater(() -> {
                alert.hide();
            });
        });
        thread.start();
        });
        btoGuardarTabla2.setOnAction(event -> {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Seleccione donde guardar el archivo.");
            File tmp = dc.showDialog(null);
            String nuevoArchivo="";
            if(tmp!=null)
                nuevoArchivo = tmp.getAbsolutePath();
            if(nuevoArchivo!="")
                nuevoArchivo+='\\'+txtNombreArchivo.getText()+".xlsx";
            fileCombinaciones = new File(nuevoArchivo);

            try{
                fileCombinaciones.createNewFile();
            }catch (IOException ioe){}
            Thread thread = new Thread(() -> {
                Platform.runLater(() -> {
                    alert.showAndWait();
                });
                writeExcelFile(fileCombinaciones,itemsTabla2);
                txtNombreArchivo.setText("Combinaciones");
                Platform.runLater(() -> {
                    alert.hide();
                });
            });
            thread.start();
        });
        btoAgregar.setOnAction(event -> {
            agregarCasilla();
        });
        btoSelectNinguno.setOnAction(event -> {
            selectTodos(false);
        });
        btoSelectTodos.setOnAction(event -> {
            selectTodos(true);
        });
        btoListo.setOnAction(event -> {
            Thread thread = new Thread(() -> {
                Platform.runLater(() -> {
                    alert.showAndWait();
                });
                List<String> aa = new ArrayList<String>();
                for (int i = 0; i < 37; i++) {
                    aa.add("" + i);
                }
                setDataTable1(combinar(1,aa, 5));
            Platform.runLater(() -> {
                alert.hide();
            });
        });
        thread.start();
        });
        btoListoedit.setOnAction(event -> {
            Thread thread = new Thread(() -> {
                Platform.runLater(() -> {
                    alert.showAndWait();
                });
                ArrayList<String> al = getNumeros();
                if(al!=null && al.size()>4)  setDataTable2(combinar(0,al,5));
                Platform.runLater(() -> {
                    alert.hide();
                });
            });
            thread.start();
        });
        btoEliminarCombinacion.setOnAction(event -> {
            Combinacion c = tableCombinaciones.getSelectionModel().getSelectedItem();
            eliminarCombinacion(1,c);
            tableCombinaciones.setItems(items);
        });
        btoEliminaredit.setOnAction(event -> {
            Combinacion c = tableCombinacionesedit.getSelectionModel().getSelectedItem();
            eliminarCombinacion(2,c);
            tableCombinacionesedit.setItems(itemsTabla2);
        });

        btoRehacer.setOnAction(event -> {
            if(ultimaBorrada!=null){
                if(ultimaBorrada.size()>0){
                    int x = ultimaBorrada.size()-1;
                    items.add(ultimaBorrada.get(x));
                    itemsHash.add(ultimaBorrada.get(x).hash());
                    tableCombinaciones.setItems(items);
                    ultimaBorrada.remove(x);
                }
            }
        });
        btoRehaceredit.setOnAction(event -> {
            if(ultimaBorradaedit!=null){
                if(ultimaBorradaedit.size()>0){
                    int x = ultimaBorradaedit.size()-1;
                    itemsTabla2.add(ultimaBorradaedit.get(x));
                    tableCombinacionesedit.setItems(itemsTabla2);
                    ultimaBorradaedit.remove(x);
                }
            }
        });
    }
    public void eliminarCombinacion(int tipo, Combinacion comb){
        if(comb!=null){
            if(tipo==1){
                ultimaBorrada.add(comb);
                itemsHash.remove((Integer)comb.hash());
                items.remove(comb);
            }
            else if(tipo==2){
                ultimaBorradaedit.add(comb);
                itemsTabla2.remove(comb);
            }
        }
    }
    public ArrayList<Combinacion> restarLista(){
        int p;
        ArrayList<Combinacion> aaa = new ArrayList<>(items);
        for(int i=0;i<itemsTabla2.size();i++){
            p=itemsHash.lastIndexOf(itemsTabla2.get(i).hash());
            if(p!=-1){
                aaa.remove(p);
                itemsHash.remove(p);
            }
        }
        setDataTable1(aaa);
        return aaa;
    }
    public Alert cargando(){
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Cargando...");
        alert.setHeaderText("Leyendo o escribiendo informacion.");
        alert.setContentText("Por favor espere.");

        alert.showAndWait();
        return alert;
    }
    public void resetChecks(){
        checkF0pares.setSelected(false);
        checkF1impares.setSelected(false);
        checkF3consecutivos5.setSelected(false);
        checkF2consecutivos3.setSelected(false);
    }
    public void agregarCasilla(){
        String str = txtFieldNuevoNumero.getText()+".";
        int cont=0,num=0,n0=0,n1=0,n2=0,n3=0,n4=0;
        int band = 0;
        ArrayList<Integer> m = new ArrayList<>();
        for(int i=0; i<str.length(); i++){
            if(str.charAt(i)>=0x30 && str.charAt(i)<=0x39){
                num=num*10+(str.charAt(i)-0x30);
            }else if(num>=0 && num <= 36){
                m.add(num);
                num=0;
                cont++;
            }else band=1;
        }
        if(band!=1){
            Collections.sort(m);
            Combinacion c = new Combinacion(m.get(0),m.get(1),m.get(2),m.get(3),m.get(4));
            if(!itemsHash.contains(c.hash())){
                itemsHash.add(c.hash());
                items.add(c);
                ArrayList<Combinacion>fk = new ArrayList<>(items);
                setDataTable1(fk);
            }
        }
    }
    public void selectTodos(Boolean a){
        num0.setSelected(a);
        num1.setSelected(a);
        num2.setSelected(a);
        num3.setSelected(a);
        num4.setSelected(a);
        num5.setSelected(a);
        num6.setSelected(a);
        num7.setSelected(a);
        num8.setSelected(a);
        num9.setSelected(a);
        num10.setSelected(a);
        num11.setSelected(a);
        num12.setSelected(a);
        num13.setSelected(a);
        num14.setSelected(a);
        num15.setSelected(a);
        num16.setSelected(a);
        num17.setSelected(a);
        num18.setSelected(a);
        num19.setSelected(a);
        num20.setSelected(a);
        num21.setSelected(a);
        num22.setSelected(a);
        num23.setSelected(a);
        num24.setSelected(a);
        num25.setSelected(a);
        num26.setSelected(a);
        num27.setSelected(a);
        num28.setSelected(a);
        num29.setSelected(a);
        num30.setSelected(a);
        num31.setSelected(a);
        num32.setSelected(a);
        num33.setSelected(a);
        num34.setSelected(a);
        num35.setSelected(a);
        num36.setSelected(a);
        }
    public void setDataTable1(ArrayList<Combinacion> lista){
        items = FXCollections.observableArrayList(lista);
        Platform.runLater(
                () -> {
                    lblTotalTabla1.setText(""+lista.size());
                }
        );
        col0.setCellValueFactory(new PropertyValueFactory<Combinacion,Integer>("n0"));
        col1.setCellValueFactory(new PropertyValueFactory<Combinacion,Integer>("n1"));
        col2.setCellValueFactory(new PropertyValueFactory<Combinacion,Integer>("n2"));
        col3.setCellValueFactory(new PropertyValueFactory<Combinacion,Integer>("n3"));
        col4.setCellValueFactory(new PropertyValueFactory<Combinacion,Integer>("n4"));
        Platform.runLater(()->{
            tableCombinaciones.setItems(items);
        });
    }
    public void setDataTable2(ArrayList<Combinacion> lista){
        Platform.runLater(
                () -> {
                    lblTotalTabla2.setText(""+lista.size());
                }
        );

        itemsTabla2 = FXCollections.observableArrayList(lista);

        col0edit.setCellValueFactory(new PropertyValueFactory<Combinacion,Integer>("n0"));
        col1edit.setCellValueFactory(new PropertyValueFactory<Combinacion,Integer>("n1"));
        col2edit.setCellValueFactory(new PropertyValueFactory<Combinacion,Integer>("n2"));
        col3edit.setCellValueFactory(new PropertyValueFactory<Combinacion,Integer>("n3"));
        col4edit.setCellValueFactory(new PropertyValueFactory<Combinacion,Integer>("n4"));
        Platform.runLater(()->{
            tableCombinacionesedit.setItems(itemsTabla2);
        });
    }
    public ArrayList<Combinacion> combinar(int tipo,List<String> a, int m) {
        if(tipo==1)
            itemsHash = new ArrayList<>();
        IteradorCombinacion it = new IteradorCombinacion(a, m);
        Iterator s = it.iterator();


        List t;
        ArrayList<Combinacion> Resultados = new ArrayList<>();
        while (s.hasNext()) {
            t = ((List) (s.next()));
            Combinacion comb = new Combinacion(
                    Integer.parseInt(t.get(4).toString()),
                    Integer.parseInt(t.get(3).toString()),
                    Integer.parseInt(t.get(2).toString()),
                    Integer.parseInt(t.get(1).toString()),
                    Integer.parseInt(t.get(0).toString()));

            Resultados.add(comb);
            if(tipo==1)
                itemsHash.add(comb.hash());
        }
        return Resultados;
    }
    public ArrayList<String> getNumeros(){
        ArrayList<String> array = new ArrayList<>();
        for(int i=0;i<37;i++){
            array.add(""+i);
        }
        if(num0.isSelected()) array.remove("0");
        if(num1.isSelected()) array.remove("1");
        if(num2.isSelected()) array.remove("2");
        if(num3.isSelected()) array.remove("3");
        if(num4.isSelected()) array.remove("4");
        if(num5.isSelected()) array.remove("5");
        if(num6.isSelected()) array.remove("6");
        if(num7.isSelected()) array.remove("7");
        if(num8.isSelected()) array.remove("8");
        if(num9.isSelected()) array.remove("9");
        if(num10.isSelected()) array.remove("10");
        if(num11.isSelected()) array.remove("11");
        if(num12.isSelected()) array.remove("12");
        if(num13.isSelected()) array.remove("13");
        if(num14.isSelected()) array.remove("14");
        if(num15.isSelected()) array.remove("15");
        if(num16.isSelected()) array.remove("16");
        if(num17.isSelected()) array.remove("17");
        if(num18.isSelected()) array.remove("18");
        if(num19.isSelected()) array.remove("19");
        if(num20.isSelected()) array.remove("20");
        if(num21.isSelected()) array.remove("21");
        if(num22.isSelected()) array.remove("22");
        if(num23.isSelected()) array.remove("23");
        if(num24.isSelected()) array.remove("24");
        if(num25.isSelected()) array.remove("25");
        if(num26.isSelected()) array.remove("26");
        if(num27.isSelected()) array.remove("27");
        if(num28.isSelected()) array.remove("28");
        if(num29.isSelected()) array.remove("29");
        if(num30.isSelected()) array.remove("30");
        if(num31.isSelected()) array.remove("31");
        if(num32.isSelected()) array.remove("32");
        if(num33.isSelected()) array.remove("33");
        if(num34.isSelected()) array.remove("34");
        if(num35.isSelected()) array.remove("35");
        if(num36.isSelected()) array.remove("36");

        return array;
    }
    public File getFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione el archivo.");
        File file = fileChooser.showOpenDialog(null);
        return file;
    }
    public ArrayList<Combinacion> readExcelFileFranc(File excelFile, ArrayList<Combinacion> existencias){
        InputStream excelStream = null;
        try {
            excelStream = new FileInputStream(excelFile);
            Workbook xssfWorkbook = new XSSFWorkbook(excelStream);
            Sheet xssfSheet = xssfWorkbook.getSheetAt(0);
            Row xssfRow;
            int c0 = 0;
            int c1 = 1;
            int c2 = 2;
            int c3 = 3;
            int c4 = 4;
            int rows = xssfSheet.getLastRowNum();
            String cellValue0="";
            String cellValue1="";
            String cellValue2="";
            String cellValue3="";
            String cellValue4="";
            int cc0=0;
            int cc1=0;
            int cc2=0;
            int cc3=0;
            int cc4=0;
            for (int r = 0; r <= rows;r++) {
                xssfRow = xssfSheet.getRow(r);
                if (xssfRow == null){
                    break;
                }else{
                    Cell celln0 = null;
                    Cell celln1 = null;
                    Cell celln2 = null;
                    Cell celln3 = null;
                    Cell celln4 = null;
                    Combinacion comb;
                    if(xssfRow!=null){
                        celln0 = xssfRow.getCell(0);
                        celln1 = xssfRow.getCell(1);
                        celln2 = xssfRow.getCell(2);
                        celln3 = xssfRow.getCell(3);
                        celln4 = xssfRow.getCell(4);
                    }
                    if(xssfRow!=null && celln0!=null && celln0.getCellType() != Cell.CELL_TYPE_BLANK && !(celln0.getCellType() == Cell.CELL_TYPE_STRING && celln0.getStringCellValue().isEmpty())) {
                        cellValue0 = xssfRow.getCell(c0) == null?"":(xssfRow.getCell(c0).getCellType() == Cell.CELL_TYPE_NUMERIC)?""+xssfRow.getCell(c0).getNumericCellValue():"";
                        cellValue0=cellValue0.substring(0,cellValue0.length()-2);
                        cc0=Integer.parseInt(cellValue0);
                    }
                    if(xssfRow!=null && celln1!=null && celln1.getCellType() != Cell.CELL_TYPE_BLANK && !(celln1.getCellType() == Cell.CELL_TYPE_STRING && celln1.getStringCellValue().isEmpty())) {
                        cellValue1 = xssfRow.getCell(c1) == null?"":(xssfRow.getCell(c1).getCellType() == Cell.CELL_TYPE_NUMERIC)?""+xssfRow.getCell(c1).getNumericCellValue():"";
                        cellValue1=cellValue1.substring(0,cellValue1.length()-2);
                        cc1=Integer.parseInt(cellValue1);
                    }
                    if(xssfRow!=null && celln2!=null && celln2.getCellType() != Cell.CELL_TYPE_BLANK && !(celln2.getCellType() == Cell.CELL_TYPE_STRING && celln2.getStringCellValue().isEmpty())) {
                        cellValue2 = xssfRow.getCell(c2) == null?"":(xssfRow.getCell(c2).getCellType() == Cell.CELL_TYPE_NUMERIC)?""+xssfRow.getCell(c2).getNumericCellValue():"";
                        cellValue2=cellValue2.substring(0,cellValue2.length()-2);
                        cc2=Integer.parseInt(cellValue2);
                    }
                    if(xssfRow!=null && celln3!=null && celln3.getCellType() != Cell.CELL_TYPE_BLANK && !(celln3.getCellType() == Cell.CELL_TYPE_STRING && celln3.getStringCellValue().isEmpty())) {
                        cellValue3 = xssfRow.getCell(c3) == null?"":(xssfRow.getCell(c3).getCellType() == Cell.CELL_TYPE_NUMERIC)?""+xssfRow.getCell(c3).getNumericCellValue():"";
                        cellValue3=cellValue3.substring(0,cellValue3.length()-2);
                        cc3=Integer.parseInt(cellValue3);
                    }
                    if(xssfRow!=null && celln4!=null && celln4.getCellType() != Cell.CELL_TYPE_BLANK && !(celln4.getCellType() == Cell.CELL_TYPE_STRING && celln4.getStringCellValue().isEmpty())) {
                        cellValue4 = xssfRow.getCell(c4) == null?"":(xssfRow.getCell(c4).getCellType() == Cell.CELL_TYPE_NUMERIC)?""+xssfRow.getCell(c4).getNumericCellValue():"";
                        cellValue4=cellValue4.substring(0,cellValue4.length()-2);
                        cc4=Integer.parseInt(cellValue4);
                    }
                    comb = new Combinacion(cc0,cc1,cc2,cc3,cc4);
                    existencias.add(comb);
                }
            }
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                excelStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return existencias;
    }
    public void writeExcelFile(File excelNewFile,ObservableList<Combinacion> lista){
        OutputStream excelNewOutputStream = null;
        try{
            excelNewOutputStream = new FileOutputStream(excelNewFile);
            Workbook xssfWorkbookNew = new XSSFWorkbook();
            Sheet xssfSheetNew = xssfWorkbookNew.createSheet("Filter");
            Row xssfRowNew;
            Cell celln0;
            Cell celln1;
            Cell celln2;
            Cell celln3;
            Cell celln4;
            for (int r = 0; r < lista.size();r++){
                xssfRowNew = xssfSheetNew.createRow(r);
                celln0 = xssfRowNew.createCell(0);
                celln1 = xssfRowNew.createCell(1);
                celln2 = xssfRowNew.createCell(2);
                celln3 = xssfRowNew.createCell(3);
                celln4 = xssfRowNew.createCell(4);
                celln0.setCellType(Cell.CELL_TYPE_NUMERIC);
                celln1.setCellType(Cell.CELL_TYPE_NUMERIC);
                celln2.setCellType(Cell.CELL_TYPE_NUMERIC);
                celln3.setCellType(Cell.CELL_TYPE_NUMERIC);
                celln4.setCellType(Cell.CELL_TYPE_NUMERIC);
                celln0.setCellValue(lista.get(r).getN0());
                celln1.setCellValue(lista.get(r).getN1());
                celln2.setCellValue(lista.get(r).getN2());
                celln3.setCellValue(lista.get(r).getN3());
                celln4.setCellValue(lista.get(r).getN4());

            }
            xssfWorkbookNew.write(excelNewOutputStream);
            excelNewOutputStream.close();
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}

class IteradorCombinacion implements Iterable<List<String>> {

    private List<String> lista;
    private Integer k;

    public IteradorCombinacion(List<String> s, Integer k) {
        lista = s;
        this.k = k;
    }

    @Override
    public Iterator<List<String>> iterator() {
        return new IteradorCombn(lista, k);
    }

    private class IteradorCombn implements Iterator<List<String>> {

        private int actualSize, maxresult;
        private Integer curIndex;
        private String[] result;
        private int[] indices;
        private String[] arrayList;
        private List<String> elem = null;

        public IteradorCombn(List<String> s, Integer k) {
            actualSize = k;// desde dónde
            curIndex = 0;
            maxresult = k;
            arrayList = new String[s.size()];
            for (int i = 0; i < arrayList.length; i++) { // la lista s la vuelca en arrayList
                arrayList[i] = s.get(i);
            }
            this.result = new String[actualSize < s.size() ? actualSize : s.size()];
            //el tamaño de result va a ser el valor menor entre actualSize y el tamaño de s
            indices = new int[result.length];

            for (int i = 0; i < result.length; i++) {
                indices[i] = result.length - 2 - i;
            }
        }

        public boolean hasNext() {
            elem = null;
            while ((elem == null && curIndex != -1)) {

                indices[curIndex]++;
                if (indices[curIndex] == (curIndex == 0 ? arrayList.length : indices[curIndex - 1])) {

                    indices[curIndex] = indices.length - curIndex - 2;
                    curIndex--;
                } else {

                    result[curIndex] = arrayList[indices[curIndex]];

                    if (curIndex < indices.length - 1) {
                        curIndex++;
                    } else {
                        elem = new LinkedList<String>();
                        for (String s : result) {
                            elem.add(s);

                        }
                    }
                }
            }

            if (elem == null) {
                if (actualSize < maxresult) {
                    actualSize++;
                    this.result = new String[actualSize < arrayList.length ? actualSize : arrayList.length];
                    indices = new int[result.length];

                    for (int i = 0; i < result.length; i++) {

                        indices[i] = result.length - 2 - i;
                    }
                    curIndex = 0;

                    return this.hasNext();
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }

        @Override
        public List<String> next() {
            return elem;
        }

        @Override
        public void remove() {
        }
    }
}