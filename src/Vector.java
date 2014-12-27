import java.text.DecimalFormat;


public class Vector {
	private int num;
	private double[] pdata;

	private void Init(int Num)
	{
		//check input sanity
		if(Num < 0) throw new IllegalArgumentException("vector size negative");
		num = Num;
		if (num <= 0)
			pdata = new double[0];  // empty vector, nothing to allocate
		else {
			pdata = new double[num];  // Allocate memory for vector
			for (int i=0; i<num; i++) pdata[i] = 0.0;
		}
	}

	public Vector() {
		this.num = 0;
		this.pdata = new double[0];
	}

	public Vector(int Num) {
		Init(Num);
	}

	public Vector(Vector copy)
	{
		Init(copy.size());
		for(int i=0; i<num; i++)
			pdata[i] = copy.pdata[i];
	}
	
	public int size() {
		return this.num;
	}
	/*
	public Vector assign(Vector copy) throws CloneNotSupportedException {
		if(this.equals(copy))
			return this;
		return (Vector)copy.clone();
	}*/
	
	public double get(int i) {
		if(i<0 || i>=num)
			throw new IndexOutOfBoundsException("vector access error");
		return pdata[i];
	}
	
	public void set(int i, double value) {
		if(i<0 || i>=num)
			throw new IndexOutOfBoundsException("vector access error");
		pdata[i] = value;
	}
	
	public boolean equiv(Vector v, double tol)
	{
		if(num != v.num)
			throw new IllegalArgumentException("incompatible vector size\n");
		for(int i=0; i<num; i++)
			if(Math.abs(pdata[i]-v.get(i)) > tol)
				return false;
		return true;
	}
	
	public String toString() {
		String res = "";
		for(int i=0; i<pdata.length; i++)
		{
			res += "  " + new DecimalFormat("#0.0000000").format(pdata[i]);
		}
		return res+"\n";
	}
	
	
}
