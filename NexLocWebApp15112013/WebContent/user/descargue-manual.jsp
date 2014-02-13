<%@ page import="java.util.*,java.io.*"%>               

<%
  String root = getServletContext().getRealPath("/"); 
  File f = new File (root+"/user/documentos/Manual_de_Usuario_Aplicacione_Web.pdf");
  response.setContentType ("application/pdf");
  response.setHeader ("Content-Disposition", "attachment; filename=ManualUsuarioWeb.pdf");
  String name = f.getName().substring(f.getName().lastIndexOf("/") + 1,f.getName().length());
  InputStream in = new FileInputStream(f);
  ServletOutputStream outs = response.getOutputStream();


  int bit = 256;
  int i = 0;
  try {
  while ((bit) >= 0) {
  bit = in.read();
  outs.write(bit);
                     }
       } 
        catch (IOException ioe) {
                ioe.printStackTrace(System.out);
            }
   outs.flush();
   outs.close();
   in.close();   
 %>