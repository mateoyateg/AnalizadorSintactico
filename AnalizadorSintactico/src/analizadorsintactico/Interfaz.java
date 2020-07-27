/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorsintactico;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author mateo
 */
public class Interfaz implements ActionListener{
    
    JFrame ventana;
    JLabel lbNombres;
    JTextField tfIngreso;
    JButton btAvanzar;
    String cadena;
    int numeroColumnasActual;
    
    public Interfaz() {
        ventana = new JFrame("");
        
        ventana.setSize(700, 180);
        ventana.setTitle("Analizador Sintactico MySQL");
        
        tfIngreso = new JTextField("");
        tfIngreso.setBounds(90, 25, 500, 25);
        tfIngreso.setFont(new java.awt.Font("Cambria", 0, 12));
        ventana.add(tfIngreso);
        
        btAvanzar = new JButton("Verificar");
        btAvanzar.setBounds(270, 60, 120, 30);
        btAvanzar.addActionListener(this);
        ventana.add(btAvanzar);
        
        lbNombres = new JLabel("Daniela A. Martinez - Mateo Yate G. - Daniel A. Roa / Ciencias III");
        lbNombres.setBounds(180, 100, 500, 10);
        lbNombres.setFont(new java.awt.Font("Cambria", 0, 12));
        ventana.add(lbNombres);
        
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(null);
        
        ventana.setVisible(true);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btAvanzar){
            if (tfIngreso.getText().length() != 0){
                
                System.out.println("El campo de texto no está vacio.");
                
                String[] cadenaSeparada = tfIngreso.getText().split(" ");
                
                for (int i = 0; i < cadenaSeparada.length; i++){
                    System.out.println(cadenaSeparada[i]);
                }
                
                String opc = cadenaSeparada[0];
                
                switch (opc){
                    case "INSERT":
                        insert(cadenaSeparada);
                        break;
                    case "DELETE":
                        delete(cadenaSeparada);
                        break;
                    case "UPDATE":
                        update(cadenaSeparada);
                        break;
                    case "SELECT":
                        select(cadenaSeparada);
                        break;
                    case "CREATE":
                        create(cadenaSeparada);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Expresión errada: La primera palabra no es un statement");
                }
                
            } else {
                JOptionPane.showMessageDialog(null, "El campo de texto está vacio");
            }
        }
    }
    public void select(String[] cadenaSeparada) {
        String valorTabla;
        int valorFrom = 0;
        
        Boolean error = false;
        if( cadenaSeparada.length> 1 && cadenaSeparada[0].equals("SELECT")){
            System.out.println("El nombre de la sentencia es correcto y hay algo después");
            for(int i=1; i<cadenaSeparada.length; i++) {
                if(cadenaSeparada[i].equals("FROM")){
                    valorFrom = i;
                }
            }
            if(cadenaSeparada[valorFrom].equals("FROM") && cadenaSeparada.length> valorFrom) {
                if(cadenaSeparada.length > valorFrom +1 && verificarNombre(cadenaSeparada[valorFrom+1]) ) {
                    valorTabla = cadenaSeparada[valorFrom+1];
                    for(int j=1; j<valorFrom; j++) {
                        if(!verificarCamposTabla(cadenaSeparada[j], valorTabla)) {
                            error = true;
                        }
                    }
                    if(!error || cadenaSeparada[valorFrom-1].equals("*") ) {
                        
                        if(cadenaSeparada.length>valorFrom+2) {
                            if(cadenaSeparada[valorFrom+2].equals("WHERE")) {
                                if( cadenaSeparada.length> valorFrom+3) {
                                    if(verificarCamposTabla(cadenaSeparada[valorFrom+3], valorTabla)) {
                                        if(cadenaSeparada.length> valorFrom+4 && cadenaSeparada[valorFrom+4].equals("=")){
                                            if(cadenaSeparada.length> valorFrom+5 && verificarNombre(cadenaSeparada[valorFrom+5]) ) {
                                                JOptionPane.showMessageDialog(null, "Expresion valida ");
                                            } else {
                                                JOptionPane.showMessageDialog(null, "Falta valor de condicion");
                                            }
                                        } else {
                                           JOptionPane.showMessageDialog(null, "Falta operador de condicion");

                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(null, "No hay referencia a la tabla a consultar");
                                    }
                                    
                                } else {
                                     JOptionPane.showMessageDialog(null, "Falta condicion");
                                }
                            } else {
                                  JOptionPane.showMessageDialog(null, "Sentencia erronea..Estas intentado escribir WHERE?");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Expresion valida ");
                        }
                    } else{
                      JOptionPane.showMessageDialog(null, "Mala sintaxis de columnas requeridas ");
                    }
                } else{
                    JOptionPane.showMessageDialog(null, "Sentencia reservada ");

                }
            }
            
        } else {
            
                 JOptionPane.showMessageDialog(null, "Solo hay una palabra reservada, continue con la expresion ");

            
        }
    }
    
    public void create(String[] cadenaSeparada) {
        int valorCierre;
        if(cadenaSeparada.length > 1 && cadenaSeparada[0].equals("CREATE") ){
            if(cadenaSeparada[1].equals("TABLE")) {
                if(cadenaSeparada.length> 2 && verificarNombre(cadenaSeparada[2])) {
                    if(cadenaSeparada.length>3 && cadenaSeparada[3].startsWith("{")){
                        
                        if(verficarDeclaraciones(cadenaSeparada)) {
                            JOptionPane.showMessageDialog(null, "Expresion valida");
                        }
                    } else {
                                        JOptionPane.showMessageDialog(null, "No ha declarado bien los parametros");

                    }                       
                     
                } else {
                                     JOptionPane.showMessageDialog(null, "Palabra reservada ");

                }
            } else {
                                 JOptionPane.showMessageDialog(null, "No contiene la palabra TABLA ");

            }
        } else {
                             JOptionPane.showMessageDialog(null, "Solo hay una palabra reservada, continue con la expresion ");

        }
    }
    
    public boolean verficarDeclaraciones(String[] cadena) {
        String cad=cadena[3];
        String[] nueva = new String[]{};
        String[] otra = new String[]{};
        Boolean condicion = false;
        Boolean otraCondicion = false;
        for(int x=4; x< cadena.length; x++){
             cad+=" "+cadena[x];
         }
        String requiredString = cad.substring(cad.indexOf("{") + 1, cad.indexOf("}"));
        if(requiredString.contains(";")) {
            condicion = false;
        }
        nueva = requiredString.split(",");
        
        for(int y= 0; y< nueva.length; y++) {
            otra = nueva[y].split(" ");
            System.out.println(otra.length);
            if(otra.length < 2) {
                System.out.println("no contiene una definicion de variable");
                condicion = false;
            } else {
                
                //Pattern pat = Pattern.compile("CHAR|BINARY|BOOL|SMALLINT|INTEGER|FLOAT");
                //Matcher mat = pat.matcher(otra[1]);
                System.out.println(otra[1]);
                if(otra[1].contains("CHAR")) {
                    System.out.println(" contiene tipos de variable");
                    otraCondicion = true;
                } else {
                    System.out.println("no contiene tipos de variable");
                    otraCondicion = false;
                }
            }
        }
        return !condicion? condicion: otraCondicion;
    }

    public void update(String[] cadenaSeparada){
        if (verificarNombre(cadenaSeparada[1]) && cadenaSeparada.length > 2){
            System.out.println("El nombre de la tabla es correcto y hay algo después");
            
            if (cadenaSeparada[2].equals("SET") && cadenaSeparada.length > 3){
                System.out.println("La expresión contiene SET y hay algo después");
                
                if (verificarIngresos(cadenaSeparada[3]) && cadenaSeparada.length > 4){
                    System.out.println("Los ingresos estan bien escritos");
                    
                    if (cadenaSeparada[4].equals("WHERE") && cadenaSeparada.length > 4) {
                        System.out.println("La expresión contiene WHERE y hay algo después");

                        if (cadenaSeparada.length > 6) {
                            cadenaSeparada[5] = cadenaSeparada[5] + cadenaSeparada[6];
                            System.out.println("Concatene YEI");
                        }

                        if (verificarCondicion(cadenaSeparada[5])) {
                            System.out.println("La expresion es valida");
                            JOptionPane.showMessageDialog(null, "Expresion VALIDA");
                        } else {
                            JOptionPane.showMessageDialog(null, "La condicion NO ES VALIDA");
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "La expresion no contiene WHERE o no tiene nada despues de este");
                    }
                    
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Expresion invalida por condiciones");
                }
                
            } else {
                JOptionPane.showMessageDialog(null, "Expresion sin un SET");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Expresion errada en el nombre de la tabla");
        }
    }
    
    public void insert(String[] cadenaSeparada) {

        if (cadenaSeparada[1].equals("INTO") && cadenaSeparada.length > 2) {
            System.out.println("La expresión contiene INTO y hay algo después");

            if (verificarNombre(cadenaSeparada[2]) && cadenaSeparada.length > 3) {
                System.out.println("El nombre de la tabla es correcto y hay algo después");

                if (verificarColumnas(cadenaSeparada[3]) && cadenaSeparada.length > 4) {
                    System.out.println("Las columnas son correctas y hay algo después");

                    if (cadenaSeparada[4].equals("VALUES") && cadenaSeparada.length > 5) {
                        System.out.println("La expresión contiene VALUES y hay algo después");
                        
                        if (verificarValores(cadenaSeparada[5])){
                            System.out.println("La expresion es valida");
                            JOptionPane.showMessageDialog(null, "Expresion VALIDA");
                        } else {
                            JOptionPane.showMessageDialog(null, "Expresion errada en la expresion de los valores a insertar");
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Expresion errada en la expresion VALUES");
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Expresion errada en las columnas de la table");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Expresion errada en el nombre de la tabla");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Expresion errada en la expresion INTO");
        }
    }
    
    public void delete(String[] cadenaSeparada) {
        if (cadenaSeparada[1].equals("FROM") && cadenaSeparada.length > 2){
            System.out.println("La expresión contiene FROM y hay algo después");
            
            if (verificarNombre(cadenaSeparada[2]) && cadenaSeparada.length > 3) {
                System.out.println("Hay algo más, la expresion no para aca");
                
                if (cadenaSeparada[3].equals("WHERE") && cadenaSeparada.length > 4){
                    System.out.println("La expresión contiene WHERE y hay algo después");
                    
                    if (cadenaSeparada.length > 5){
                        cadenaSeparada[4] = cadenaSeparada[4] + cadenaSeparada[5];
                        System.out.println("Concatene YEI");
                    }
                    
                    if (verificarCondicion(cadenaSeparada[4])) {
                        System.out.println("La expresion es valida");
                        JOptionPane.showMessageDialog(null, "Expresion VALIDA");
                    } else {
                        JOptionPane.showMessageDialog(null, "La condicion NO ES VALIDA");
                    }
                    
                } else {
                    JOptionPane.showMessageDialog(null, "La expresion no contiene WHERE o no tiene nada despues de este");
                }
                
            } else if (verificarDeleteCorto(cadenaSeparada[2])) {
                JOptionPane.showMessageDialog(null, "Expresion VALIDA");
            } else {
                JOptionPane.showMessageDialog(null, "El nombre de la tabla no es valido o faltan mas datos...");
            }
            
        } else {
            JOptionPane.showMessageDialog(null, "Expresion errada en la expresion FROM");
        }
    }
    
    public boolean verificarIngresos(String tbSet){
        String[] separaComas = tbSet.split(",");
        
        for (int i=0; i<separaComas.length; i++){
            return verificarSeteada(separaComas[i]);
        }
        
        return true;
        
    }
    
    public boolean verificarSeteada(String tbCondicion){
        System.out.println("Condicion a verificar: " + tbCondicion);
        System.out.println("");
        Pattern pat = Pattern.compile("^[^\\d]*.*=[\"']*.*[\"']*");
        Matcher mat = pat.matcher(tbCondicion);
        
        if (mat.matches()){
            System.out.println("La estructura de la condicion es correcta");
            
            pat = Pattern.compile("UPDATE|CREATE|SELECT|INSERT|DELETE|"
                    + "\\(|\\)|/|&|#|>|<|\\{|\\}");
            mat = pat.matcher(tbCondicion);

            if (!mat.find()) {
                System.out.println(tbCondicion + ": No contiene palabras reservadas");
                return true;

            } else {
                System.out.println("ERROR: " + tbCondicion + " contiene palabras reservadas");
                return false;
            }
            
        } else {
            System.out.println("La estructura de la condicion no es correcta");
            return false;
        }
    }
    
    public boolean verificarCamposTabla(String valores, String tabla) {
        System.out.println(valores);
        
        String[] columnas = new String[]{};
                columnas = valores.split("\\,");
        String[] nombreTabla = columnas ;
        Boolean error = false;
        
        for(int i=0; i<columnas.length; i++) {
            System.out.println(columnas[i]);
            nombreTabla[i] = columnas[i].split("\\.")[0];
        }
        
        if(nombreTabla.length< columnas.length) {
            return false;
        } else {
            for(int j= 0; j<nombreTabla.length; j++) {
                System.out.println(nombreTabla[j]);
                
                if(!nombreTabla[j].equals(tabla)) {
                    error = true;
                }
                
            }
             System.out.println(error);
             return error? false: true;  
        }

    }
    
    public boolean verificarCondicion(String tbCondicion){
        System.out.println("Condicion a verificar: " + tbCondicion);
        System.out.println("");
        Pattern pat = Pattern.compile("^[^\\d].*=[\"']*.*[\"']*;$");
        Matcher mat = pat.matcher(tbCondicion);
        
        if (mat.matches()){
            System.out.println("La estructura de la condicion es correcta");
            
            pat = Pattern.compile("UPDATE|CREATE|SELECT|INSERT|DELETE|"
                    + "\\(|\\)|/|&|#|>|<|\\{|\\}");
            mat = pat.matcher(tbCondicion);

            if (!mat.find()) {
                System.out.println(tbCondicion + ": No contiene palabras reservadas");
                return true;

            } else {
                System.out.println("ERROR: " + tbCondicion + " contiene palabras reservadas");
                return false;
            }
            
        } else {
            System.out.println("La estructura de la condicion no es correcta");
            return false;
        }
    }
    
    public boolean verificarDeleteCorto(String tbName) {

        //Verifica que el nombre no contenta palabras reservadas
        Pattern pat = Pattern.compile("UPDATE|CREATE|SELECT|INSERT|DELETE|"
                + "\\(|\\)|\\+|\\-|/|\\*|\\\"|=|&|#|>|<|\\^|'|\\{|\\}|%");
        //Pattern pat = Pattern.compile("^[^\\d]{0,}.[^INSERT,CREATE,DELETE,if,else,for].");
        Matcher mat = pat.matcher(tbName);
        //Esta bien
        if (!mat.find()) {
            System.out.println(tbName + ": No contiene palabras reservadas");
            pat = Pattern.compile("^[^\\d]*.*[;]$");
            mat = pat.matcher(tbName);
            if (mat.matches()) {
                System.out.println(tbName + ": No empieza por un digito y termina en punto y coma");
                return true;
                
            } else {
                System.out.println(tbName + ": Empieza por un digito o no tiene punto y coma final");
                return false;
            }

        } else {
            System.out.println("ERROR: " + tbName + " contiene palabras reservadas");
            return false;
        }

    }
    
    public boolean verificarNombre(String tbName) {

        //Verifica que el nombre no contenta palabras reservadas
        Pattern pat = Pattern.compile("UPDATE|CREATE|SELECT|INSERT|DELETE|"
                + "\\(|\\)|\\+|\\-|/|\\*|\\\"|=|&|#|>|<|\\^|'|\\{|\\}|%");
        //Pattern pat = Pattern.compile("^[^\\d]{0,}.[^INSERT,CREATE,DELETE,if,else,for].");
        Matcher mat = pat.matcher(tbName);
        //Esta bien
        if (!mat.find()) {
            System.out.println(tbName + ": No contiene palabras reservadas");
            pat = Pattern.compile("^[^\\d]*.*");
            mat = pat.matcher(tbName);
            if (mat.matches()) {
                System.out.println(tbName + ": No empieza por un digito");
                return true;
                
            } else {
                System.out.println(tbName + ": Empieza por un digito");
                return false;
            }

        } else {
            System.out.println("ERROR: " + tbName + " contiene palabras reservadas");
            return false;
        }

    }
    
    public boolean verificarValores(String tbValores) {
        Pattern pat = Pattern.compile("^[(]*.*[^,]+[)][;]$");
        Matcher mat = pat.matcher(tbValores);

        tbValores = tbValores.replaceAll("\\(", "");
        tbValores = tbValores.replaceAll("\\)", "");
        String[] separaComas = tbValores.split(",");
        
        if (separaComas.length == numeroColumnasActual){
            System.out.println("El numero de columnas de los valores coincide...");

            pat = Pattern.compile("UPDATE|CREATE|SELECT|INSERT|DELETE|"
                    + "\\(|\\)|/|\\\"|&|#|>|<|'|\\{|\\}");
            mat = pat.matcher(tbValores);

            if (!mat.find()) {
                System.out.println(tbValores + ": No contiene palabras reservadas");
                return true;

            } else {
                System.out.println("ERROR: " + tbValores + " contiene palabras reservadas");
                return false;
            }
            
        } else {
            System.out.println("NO coinciden el numero de valores con el numero de columnas");
            return false;
        }

    }
    
    public boolean verificarColumnas(String tbColumnas) {
        Pattern pat = Pattern.compile("^[(]*.*[^,]+[)]$");
        Matcher mat = pat.matcher(tbColumnas);
        //Esta bien
        if (mat.matches()) {
            System.out.println("La estructura de parentesis de columnas esta bien");
            tbColumnas = tbColumnas.replaceAll("\\(", "");
            tbColumnas = tbColumnas.replaceAll("\\)", "");
            String[] separaComas = tbColumnas.split(",");
            

            for (int i = 0; i < separaComas.length; i++) {
                if (verificarNombre(separaComas[i])) {
                    System.out.println("Columa " + i + " esta bien");
                    //do nothing
                } else {
                    System.out.println("Error: La columna " + i + " tiene un formato invalido");
                    return false;
                }
            }
            
            if (separaComas.length == 1 && separaComas[0].equals("")){
                
                numeroColumnasActual = 0;
            } else {
                
                numeroColumnasActual = separaComas.length;
            }
            
            return true;
        } else {
            System.out.println("Error: la estructura de parentesis de columnas esta mal ");
            return false;
        }
    }

}
