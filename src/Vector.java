import java.text.DecimalFormat;

/*
 *  Vector class
 */
public class Vector {
	/*
	 *  Size of the Vector - number of elements it has
	 */
	private int num;
	
	/*
	 *  An array for storing the elements of Vector
	 */
	private double[] pdata;

	/**
     * A method for allocating specific memory space for {@link Vector#pdata}. 
     * A size is given for allocating
     *
     * @param     Num  the size to allocate for {@link Vector#pdata}
     * @exception IllegalArgumentException
     *              if the given size is negative number
     * @see  Vector#num
     * @see  Vector#pdata 
     */
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

	/**
     * Vector constructor
     * 			initialize the field of the vector
     *
     * @see      Vector#num
     * @see      Vector#pdata
     */
	public Vector() {
		this.num = 0;
		this.pdata = new double[0];
	}

	/**
     * Vector constructor, created a new Vector with a given size
     *
     * @param    Num  the size for the new Vector
     * @see      Vector#Init(int)
     */
	public Vector(int Num) {
		Init(Num);
	}

	/**
     * Copy constructor, created a new Vector by copying data from a given Vector
     *
     * @param    copy Vector object where to copy from
     * @see      Vector#pdata
     */
	public Vector(Vector copy)
	{
		Init(copy.size());
		for(int i=0; i<num; i++)
			pdata[i] = copy.pdata[i];
	}
	
	/**
     * Returns the size of Vector.
     *
     * @return    the size of Vector - number of elements it has
     * @see       Vector#num
     */
	public int size() {
		return this.num;
	}

	/**
     * Returns the element at the specified index. An index
     * ranges from <code>0</code> to <code>length() - 1</code>.
     *
     * @param     i  the index of the desired element.
     * @return    the desired element.
     * @exception IndexOutOfBoundsException
     *              if the index is not in the range <code>0</code>
     *              to <code>length()-1</code>.
     */
	public double get(int i) {
		if(i<0 || i>=num)
			throw new IndexOutOfBoundsException("vector access error");
		return pdata[i];
	}
	
	/**
     * Set the value of element at the specified index. An index
     * ranges from <code>0</code> to <code>length() - 1</code>.
     *
     * @param     i  the index of the desired element.
     * @param     value the value to assign.
     * @exception IndexOutOfBoundsException
     *              if the index is not in the range <code>0</code>
     *              to <code>length()-1</code>.
     */
	public void set(int i, double value) {
		if(i<0 || i>=num)
			throw new IndexOutOfBoundsException("vector access error");
		pdata[i] = value;
	}
	
	/**
     * A method to compare the equality of a given Vector with current Vector. 
     * an tolerant value is given for checking the equality.
     *
     * @param     v  a given Vector to check equality with current one
     * @param     tol a tolerant value for assuming equality
     * @exception IllegalArgumentException
     *              if the size of given Vector v does not have same size as current Vector
     */
	public boolean equiv(Vector v, double tol)
	{
		if(num != v.num)
			throw new IllegalArgumentException("incompatible vector size\n");
		for(int i=0; i<num; i++)
			if(Math.abs(pdata[i]-v.get(i)) > tol)
				return false;
		return true;
	}
	
	/**
     * A method for formating Vector's data into a string,
     * for each element of Vector, format it as a 7 decimal double number.
     *
     * @return formated string
     */
	public String toString() {
		String res = "";
		for(int i=0; i<pdata.length; i++)
		{
			res += "  " + new DecimalFormat("#0.0000000").format(pdata[i]);
		}
		return res+"\n";
	}
	
}
