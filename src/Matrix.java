import java.text.DecimalFormat;


public class Matrix {
	private Vector v;
	int nrows;
	int ncols;

	public Matrix() {
		nrows = 0;
		ncols = 0;
		v = new Vector(0);
	}

	public Matrix(int Nrows, int Ncols) {
		if(Nrows < 0 || Ncols < 0)
			throw new IllegalArgumentException("matrix size negative");
		nrows = Nrows;
		ncols = Ncols;

		v = new Vector(nrows*ncols);
	}

	public Matrix(Vector x){
		v=x;
		nrows = x.size();
		ncols = 1;
	}

	public Matrix(Matrix m){
		v = new Vector(m.v);
		nrows = m.nrows;
		ncols = m.ncols;
	}

	public double get(int i, int j) {
		if(i>nrows-1 || j>ncols-1 || i<0 || j<0)
			throw new IndexOutOfBoundsException("matrix access error");
		return v.get(i*ncols+j);
	}

	public void set(int i, int j, double value)
	{
		if(i>nrows-1 || j>ncols-1 || i<0 || j<0)
			throw new IndexOutOfBoundsException("matrix access error");
		v.set(i*ncols+j, value);
	}
	/*
	public Matrix assign(Matrix m) {
		nrows = m.nrows;
		ncols = m.ncols;

		v = m.v;  //TODO: verify if = work
		return this;
	}
	 */
	public Matrix mult(Matrix m) {
		if(ncols != m.nrows)
			throw new IllegalArgumentException("matrix size does not match");

		Matrix matrix = new Matrix(nrows, m.ncols);

		for(int i=0; i<nrows; i++)
			for(int j=0; j<matrix.ncols; j++)
				matrix.set(i, j, 0);

		for (int i=0;i<nrows;i++) {
			for (int j=0;j<m.ncols;j++) {
				for (int k=0;k<ncols;k++) {
					//matrix(i,j) += ((*this)(i,k) * a(k,j));
					matrix.set(i, j, matrix.get(i, j) + this.get(i, k) * m.get(k, j));
				}
			}
		}
		return matrix;
	}

	public Vector mult(Vector v) {
		if(ncols != v.size())
			throw new IllegalArgumentException("matrix and vector size not match");

		Vector res = new Vector(nrows);

		for(int i=0; i<nrows; i++)
		{
			double aux = 0.0;
			for(int j=0; j<ncols; j++)
				aux += this.get(i, j) * v.get(j);

			res.set(i, aux);
		}
		return res;
	}

	public boolean equiv(Matrix a, double tol) {

		//if the sizes do not match return false*
		if ( (nrows != a.nrows) || (ncols != a.ncols) ) return false;

		//compare all of the elements
		for (int i=0;i<nrows;i++) {
			for (int j=0;j<ncols;j++) {
				if (Math.abs(this.get(i, j) - a.get(i,j)) > tol) 
					return false; 
			}
		}
		return true;
	}

	public Matrix transpose()
	{
		Matrix temp = new Matrix(nrows, ncols);

		for (int i=0; i<nrows; i++)
			for (int j=0; j<ncols; j++)
				temp.set(i,j,this.get(j,i));
		return temp;
	}

	public void lu_fact(Matrix a, Matrix l, Matrix u, int n)
	{
		Matrix temp = new Matrix(a);
		
		double mult;
		// LU (Doolittle's) decomposition without pivoting
		for (int k = 0; k < n - 1; k++) {
			for (int i = k + 1; i < n; i++) {
				
				if (temp.get(k,k) == 0)
				{
					System.out.print("pivot is zero 1\n");
					//System.exit(1);
				}
				mult = temp.get(i,k)/temp.get(k,k);
				temp.set(i,k,mult);                      // entries of L are saved in temp
				for (int j = k + 1; j < n; j++) {
					//temp(i,j) -= mult*temp(k,j);      // entries of U are saved in temp
					temp.set(i, j, temp.get(i, j) - mult*temp.get(k, j));
				}
			}
		}

		// create l and u from temp
		for (int i=0; i<n; i++)
			l.set(i,i,1.0);
		for (int i=1; i<n; i++)
			for (int j=0; j<i; j++)
				l.set(i,j,temp.get(i,j));

		for (int i=0; i<n; i++)
			for (int j=i; j<n; j++)
				u.set(i,j,temp.get(i,j));
	}

	public void reorder(Matrix a, int n, Matrix p)
	{
		// Note: pivoting information is stored in temperary vector pvt

		int i,j,k;
		//int* pvt;
		Vector pvt = new Vector(n);
		int pvtk,pvti;
		//double* scale;
		Vector scale = new Vector(n);
		double aet, tmp, mult;
		//double** temp;
		Matrix temp = new Matrix(a);

		for (k = 0; k < n; k++)
			pvt.set(k, k);

		for (k = 0; k < n; k++) {
			scale.set(k, 0);
			for (j = 0; j < n; j++)
				if (Math.abs(scale.get(k)) < Math.abs(temp.get(k,j))) 
					scale.set(k, Math.abs(temp.get(k,j)));
		}

		for (k = 0; k < n - 1; k++) {            // main elimination loop

			// find the pivot in column k in rows pvt[k], pvt[k+1], ..., pvt[n-1]
			int pc = k;
			aet = Math.abs(temp.get((int)pvt.get(k),k)/scale.get(k));
			for (i = k + 1; i < n; i++) {
				tmp = Math.abs(temp.get((int)pvt.get(i),k)/scale.get((int)pvt.get(i)));
				if (tmp > aet) {
					aet = tmp;
					pc = i;
				}
			}
			if (aet == 0)
			{
				System.out.print("pivot is zero 2\n");
				System.exit(1);
			}
			if (pc != k) {                      // swap pvt[k] and pvt[pc]
				int ii = (int) pvt.get(k);
				pvt.set(k, pvt.get(pc));
				pvt.set(pc, ii);
			}

			// now eliminate the column entries logically below mx[pvt[k]][k]
			pvtk = (int) pvt.get(k);                           // pivot row
			for (i = k + 1; i < n; i++) {
				pvti = (int) pvt.get(i);
				if (temp.get(pvti,k) != 0) {
					mult = temp.get(pvti,k)/temp.get(pvtk,k);
					temp.set(pvti,k,mult);
					//double aux = 0.0;
					for (j = k + 1; j < n; j++) 
						//temp(pvti,j) -= mult*temp(pvtk,j);
						//aux = temp.get(pvti, j) - mult*temp.get(pvtk, j);
						temp.set(pvti, j, temp.get(pvti, j)-mult*temp.get(pvtk, j));
				}
			}
		}
		for (i=0; i<n; i++)
			p.set(i,(int) pvt.get(i),1.0);
	}

	public String toString() {
		String res = "";
		for(int i=0; i<nrows; i++)
		{
			for(int j=0; j<ncols; j++)
			{
				res += "  " + new DecimalFormat("#0.0000000").format(this.get(i, j));
			}
			res += "\n";
		}
		return res;
	}

	public double CalcDeterminant()
	{
		if(nrows != ncols)
			throw new IllegalArgumentException("determinant: matrix must be a square matrix");

		int c, subi, i, j, subj;
		double d = 0;
		Matrix submat = new Matrix(nrows-1, ncols-1);
		if (nrows == 2) {
			return ( (this.get(0, 0) * this.get(1, 1)) - (this.get(1, 0) * this.get(0, 1)));
		}
		else
		{
			for (c = 0; c < nrows; c++) {
				subi = 0;
				for (i = 1; i < nrows; i++) {
					subj = 0;
					for (j = 0; j < nrows; j++) {
						if (j == c) {
							continue;
						}
						submat.set(subi, subj, this.get(i, j));
						subj++;
					}
					subi++;
				}
				d = d + (Math.pow(-1, c) * this.get(0, c) * submat.CalcDeterminant());
			}
		}
		return d;
	}

	public Matrix inverse(Matrix l, Matrix u)
	{
		if(this.CalcDeterminant() == 0)	
			throw new IllegalArgumentException("matrix which has determinant 0 cannot be inversed");

		Matrix p = new Matrix(this.nrows, this.ncols);
		this.reorder(this, nrows, p);

		//Matrix pa = new Matrix(p.mult(this));

		Matrix inv = new Matrix(nrows, ncols);
		Vector b = new Vector(ncols);
		Vector x = new Vector(ncols);

		for(int i=0; i<nrows; i++)
		{
			b.set(i,1.0);
			x = lu_solve(l, u, b);
			for(int j=0; j<ncols; j++)
				inv.set(j,i,x.get(j));
			b.set(i, 0.0);
		}
		return inv;
	}

	public Vector lu_solve(Matrix l, Matrix u, Vector b) 
	{
		if(!(l.nrows == u.nrows && l.ncols == b.size()))	 
			throw new IllegalArgumentException("matrix sizes do not match");

		Vector temp = new Vector(b);
		Vector x = new Vector(ncols);

		// forward substitution for L y = b.
		for(int i=1; i<ncols; i++)
		{
			double aux = temp.get(i);
			for(int j=0; j<i; j++)
				//temp[i] -= l(i,j) * temp[j];
				aux -= l.get(i, j) * temp.get(j);
			temp.set(i, aux);
		}

		// back substitution for U x = y.
		for(int i=nrows-1; i>=0; i--)
		{
			double aux = temp.get(i);
			for(int j=i+1; j<ncols; j++)
				aux -= u.get(i,j) *  temp.get(j);
			temp.set(i, aux / u.get(i,i));
		}

		// copy solution into x
		for(int i=0; i<ncols; i++)
			x.set(i,temp.get(i));

		return x;
	}

	//	public Matrix compute_lower()
	//	{
	//		Matrix temp = new Matrix(this);
	//
	//		double mult;
	//		// LU (Doolittle's) decomposition without pivoting
	//		for (int k = 0; k < nrows - 1; k++) {
	//			for (int i = k + 1; i < nrows; i++) {
	//				if (temp.get(k,k) == 0)
	//				{
	//					System.out.print("pivot is zero\n");
	//					System.exit(1);
	//				}
	//				mult = temp.get(i,k)/temp.get(k,k);
	//				temp.set(i,k,mult);                      // entries of L are saved in temp
	//				for (int j = k + 1; j < nrows; j++) {
	//					temp.set(i,j, temp.get(i,j) - mult*temp.get(k,j));      // entries of U are saved in temp
	//				}
	//			}
	//		}
	//		Matrix l = new Matrix(nrows, ncols);
	//		// create l from temp
	//		for (int i=0; i<nrows; i++) 
	//			l.set(i,i,1.0);
	//		for (int i=1; i<nrows; i++)
	//			for (int j=0; j<i; j++) 
	//				l.set(i,j,temp.get(i,j));
	//
	//		return l;
	//	}
	//	
	//	public Matrix compute_upper()
	//	{
	//		Matrix temp = new Matrix(this);
	//
	//		double mult;
	//		// LU (Doolittle's) decomposition without pivoting
	//		for (int k = 0; k < nrows - 1; k++) {
	//			for (int i = k + 1; i < nrows; i++) {
	//				if (temp.get(k,k) == 0)
	//				{
	//					System.out.print("pivot is zero\n");
	//					System.exit(1);
	//				}
	//				mult = temp.get(i,k)/temp.get(k,k);
	//				temp.set(i,k,mult);                      // entries of L are saved in temp
	//				for (int j = k + 1; j < nrows; j++) {
	//					temp.set(i,j, temp.get(i,j) - mult*temp.get(k,j));      // entries of U are saved in temp
	//				}
	//			}
	//		}
	//		Matrix u = new Matrix(nrows, ncols);
	//		// create u from temp
	//		for (int i=0; i<nrows; i++)
	//			for (int j=i; j<ncols; j++) 
	//				u.set(i,j,temp.get(i,j));
	//
	//		return u;
	//	}
	//	
	public void lu_solve(Matrix l, Matrix u, Vector b, int n, Vector x)
	{
		Vector temp = new Vector(b);

		// forward substitution for L y = b.
		for(int i=1; i<n; i++)
		{
			double aux = 0.0;
			for(int j=0; j<i; j++)
				aux -= l.get(i, j) * temp.get(j);
			temp.set(i, aux);
		}

		// back substitution for U x = y.
		for(int i=n-1; i>=0; i--)
		{
			double aux = 0.0;
			for(int j=i+1; j<n; j++)
				//temp[i] -= u(i,j) *  temp[j];
				aux -= u.get(i, j) * temp.get(j);
			temp.set(i, aux/u.get(i, i));
		}

		// copy solution into x
		for(int i=0; i<n; i++)
			x.set(i,temp.get(i));
	}

}











































