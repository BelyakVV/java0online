public class C4 {


  byte[] normalize(int[] n) {
    int i;
    for(i=0; i<n.length; i++) {
      if (n[i]>9) {
        n[i+1]+=n[i] / 10;
        n[i]=(n[i] % 10);
      }
    }
    i=n.length-1;
    while((i>=0) && (n[i]==0)) i--;
    byte[] Result = new byte[i+1];
    while(i>=0) Result[i]=(byte)n[i];
    return Result;
  }


  byte[] mmul(byte[] a, byte[] b) {
    int[] Result = new int[a.length + b.length];
    for (int i=0; i<a.length; i++) {
      for (int j=0; j<b.length; j++) {
        Result[i+j]+=a[i]*b[j];
      }
    }
    return normalize(Result);
  }


  byte[] byte2array(byte n) {
    int[] Result = new int[3];
    Result[0]=n;
    return normalize(Result);
  }


  String array2string(byte[] a) {

  }

  public static void main(String[] args) {

  }
}