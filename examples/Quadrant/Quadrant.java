/**
 * A Quadrant holds the contents of the quadrant in terms
 * of the number of each item present. Each Quadrant may
 * hold Klingons [0..3], Bases [0..1], and Stars [1..8].
 * 
 * The contents are packed into a 16-bit data type (char)
 * whoose valid range is [1..318].
 * 
 * The Klingons  value is stored in the 100's position
 * The StarBases value is stored in the  10's position
 * and the Stars value is stored in the   1's position
 * 
 * The number of Klingons in the Quadrant is the only 
 * value that may change, at this time.
 * 
 * Operations
 * 
 *  o Construct a Quadrant( k, b, s )
 *  o return the current number of Klingons
 *  o return the number of Star Bases
 *  o return the number of Stars
 *  o provide a textual presentation of the Quadrant for 
 *      use with displaying the Galactic Map eg, "318", or "001"
 *      
 *  o provides the ability to decrement the number of Klingons    
 *
 * @author Mr. Bradley
 * @version SPRING 2026
 * 
 * @ref https://support.microsoft.com/en-us/office/rnd-function-503cd2e4-3949-413f-980a-ed8fb35c1d80
 * @ref https://medium.com/swlh/code-archaeology-with-super-star-trek-928101eb010c
 */
public class Quadrant
{
    private char contents;
   
    /*** PUBLIC API ***/
    
    public Quadrant() { contents = populate(); }
    public Quadrant( int klingons, int bases, int stars ) {
        contents = encode( klingons, bases, stars);
    }
   
    public int klingons()   { return contents / 100; }
    public int bases()      { return contents / 10 % 10; }
    public int stars()      { return contents % 10; }
   
    /**
     * This method serves as a model for how reduce the number of Bases or Stars
     */
    public void reduceKlingons() {
        if( klingons() > 0 )
            contents = encode( klingons() - 1, bases(), stars() );    
    }
    
    /*** PACKAGE (DEFAULT) VISIBILITY ***/
    
    //# populate
    //# missingStarBase
    
    /*** PRIVATE ***/
    
    /**
     * this single method ensures the contraints on all Quadrant values
     * 
     * RULES:
     * =====
     * 
     *      Klingons in Quadrant are capped  @ 3  [0..3]
     *      StarBases in Quadrant are capped @ 1  [0..1]; with a Galactic max of 2.
     *      Stars in Quadrant are capped     @ 8  [1..8]
     *      
     *      Since ALL of the rules are located in this single location
     *      We do not need to set & use named constants; especially given
     *       the use of those values in error messages.
     *       
     *      Further, notice that we allow the assertion of the constraints 
     *       to halt the execution of the program -- as these are PROGRAMMING errors
     *       
     *      This method is static as it is intended as a support / encoding function.
     */
    private static char encode( int k, int b, int s ) {
        System.err.format("encode(k=%d, b=%d, s=%d).\n", k, b, s );
        //
        final int K_MIN = 0, K_MAX = 3,
                  B_MIN = 0, B_MAX = 1,
                  S_MIN = 1, S_MAX = 8;
                  
        assert isBetween( k, K_MIN, K_MAX ) : 
                String.format("klingons  [%d] < %d || > %d", k, K_MIN, K_MAX);
        assert isBetween( b, B_MIN, B_MAX ) : 
                String.format("Starbases [%d] < %d || > %d", b, B_MIN, B_MAX);
        assert isBetween( s, S_MIN, S_MAX ) : 
                String.format("Stars     [%d] < %d || > %d", s, S_MIN, S_MAX);
           
        /*        
        assert k >= 0 && k <= 3 : String.format("klingons [%d] < 0 || > 3", k);
        assert b >= 0 && b <= 1 : String.format("   bases [%d] < 0 || > 1", b);
        assert s >  0 && s <= 8 : String.format("   stars [%d] < 1 || > 8", s);
        */
        //
        char c = (char) (k*100 + b*10 + s); // K value in 100's; B value in 10's
                                            // S value is 1's
        return c;
    }
   
    @Override
    public String toString() {
        return String.valueOf( 1000 + contents ).substring(1);  // zero fills the KBS string
        // this is patterned after the same technique used in STARRTREK.BAS
    }
    //# this is the precursor version of implementing a zeroPad library function.
   
    
    /**
     * Rules for populating a Quadrant:
     * 
     *      2% chance that there are 3 Klingons present
     *      5% chance that there are 2 Klingons present
     *     20% chance that there is  1 Klingon  present
     *     83% chance that there are 0 Klingons present
     *     
     *      4% chance that there is  1 Starbase present
     *      2  Is the MAX number of Starbases present in the Galaxy
     *      
     *      There is a random (unconstrained) number of stars [1..8] present
     *      
     *      NB:
     *          This method has default visibility so it MAY be used when
     *          populating the Galaxy
     */
    static char populate() {
        /*  //#-- we want to avoid repeating the logic for the encoding
        int c = 0;
        c += genKlingons() * 100;
        c += genStarBases() * 10;
        c += genStars();
        return (char) c;
        */
        return encode( genKlingons(), genStarBases(), genStars() );
    }
    
    /**
     *  STATIC genXXX methods implement the rules for each item in a Quadrant
     *  @see Populate
     *  
     *  We demonstrate 3 techniques for generating & using random values:
     *  
     *      1) Math.random()            // genKlingons
     *      2) RandomInRange( lo, hi )  // genStars
     *      3) ChanceOf( percent )      // genStarBases
     */
    
    /**
     * 3 Ks - 2%; 2 Ks - 4%; 1 K - 20%
     * 
     * Yielding: 64 *  2% =  1.28% for 3 Klingons
     *           64 *  5% =  3.20% for 2 Klingons
     *           64 * 20% = 12.80% for 1 Klingons
     *           =================
     *                      17.28% for at least 1 Klingon
     *                      
     *           100 - 17.28 = 82.72% for 0 Klingons           
     *           
     */
    private static char genKlingons() {
        int n = 0;
        double r = Math.random();   // RND(1)
        if( r > .98 ) n++;  // .99999 - .98000 =  1.999%  (~2%)
        if( r > .95 ) n++;  // .99999 - .95000 =  4.999%  (~5%)
        if( r > .80 ) n++;  // .99999 - .80000 = 19.999% (~20%)

        return (char) n;
    }

    /*
     * We co-locate data that is used only by a _single_ method with
     * the method that uses it
     */
    private static int bases = 0;   // if 0 starbases are generated add 1 (@see galaxy)
    private static char genStarBases() {
        /*
        int n = 0;
        double r = RND(1);
        if( r > .96 ) n++;  // .999999 - 96 ~ 4%
        return (char)n;
        */
        final int MAX_STARBASES = 2;
        int n = chanceOf( 4 ) && bases < MAX_STARBASES ? 1 : 0;
        if( n == 1 ) bases++;
        return (char) n;
    }
    
    /**
     * There are two ways a Star Base can be missing:
     * 
     *  1) it was not generated via the populate method (@see populate)
     *  2) the populate method was NOT called when generating all quadrants in the Galaxy
     *  
     *  NB:
     *      This method has default visibility so it can be queried when populating the Galaxy
     */
    static boolean missingStarBase() { return bases == 0; }
    
    
    private static char genStars() {
        return (char) randomInRange( 1, 8 );
    }
    
    /**
     * LIBRARY METHODS - TO BE RELOCATED TO APPROPRIATE LIBRARY
     * 
     * returns true if a randomly generated number falls
     * within the provided percentage
     */
    
    /**
     * determines if a value is between lo and high (inclusively)
     * 
     * NB: all parameters are double, calls to narrower primitives
     *      will be upcasted. eg., isBetween( 2, 0, 9 ); // upcasted to 2.0, 0.0, 9.0
     */
    private static boolean isBetween( double value, double lo, double hi ) {
        return lo <= value && value <= hi;    
    }
    
    /**
     * returns a randomly generated value in the range [lo..hi]
     * Also: Random::nextInt(1,100)
     */
    private static int randomInRange( int lo, int hi ) {
        int n = (int) (Math.random() * (hi - lo + 1))  + lo;
        return n;
    }
    
    /**
     * Also: Random::nextDouble( 0.5, 2.7 )
     */
    private static double randomInRange( double lo, double hi ) {
        return lo + (hi - lo) * Math.random();
    }
    
    /**
     * returns true if the specified percent value < a random value [0..99].
     * eg., 3% R{0, 1, 2} is true. R(99) < 100% is always true 100% occurance.
     * 
     * NB:  .999999999 - .980000001 ~ 2 %
     */
    private static boolean chanceOf( int percent ) {
        double r = Math.random();    // [0..1)
        return r < percent/100.0;    // 100% --> [0..99] < 100
    }
    
    private static double _rnd;
    /**
     * This function is the MS BASIC function of the same name
     * Specifically, if RND is called with a non-zero value a new random
     *  value will be generated and stored in _rnd static variable (retaining its value)
     *  across calls. If RND is called with a zero value, the last random value
     *  generated is returned.
     *  
     *  NB: support for setting the seed ( < 0 parameter value) has been omitted.
     */
    private static double RND( int n ) {
        if( n != 0 ) _rnd = Math.random();  // generates a new rand
        return _rnd;                        // otherwise returns last
    }
    private static double RND() { return RND(1); }
    
    
    /** main test **/
    public static void main( String[] args ) {
        Quadrant q = new Quadrant( 3, 1, 8 );
       
        System.out.println( q );
        System.out.format("Quadrant K(%d); B(%d), S(%d). \n", q.klingons(), q.bases(), q.stars() );
        
        TEST_population(64);
        TEST_randomInRange();
    }
    
    private static void TEST_randomInRange() {
        for( int i=0; i<1000; i++ ) {
            System.out.format("%f. \n", randomInRange( 1.0, 4.0 ) );
        }
    }
    
    private static void TEST_population( int limit ) {
        int data[] = new int[limit];
        for( int i = 0; i < limit; i++ ) {
            Quadrant q = new Quadrant();
            data[i] = q.contents;
            System.out.format(" > %5d : %s.\n", i, q );
        }
        
        System.out.format("Statistics: \n");

        double sum = 0;
        double rate = 0;
        
        rate = sum = 0.0;
        for(int i : data) if( i /100 == 3 ) sum++;
        rate = sum / limit;
        System.out.format("%% klingons @ 3: %4.2f. Total: %2.0f \n", rate, rate * limit );
        
        rate = sum = 0.0;
        for(int i : data) if( i /100 == 2 ) sum++;
        rate = sum / limit;
        System.out.format("%% klingons @ 2: %4.2f. Total: %2.0f \n", rate, rate * limit );

        rate = sum = 0.0;
        for(int i : data) if( i /100 == 1 ) sum++;
        rate = sum / limit;
        System.out.format("%% klingons @ 1: %4.2f. Total: %2.0f \n", rate, rate * limit );
        
        rate = sum = 0.0;
        for(int i : data) if( i /100 == 0 ) sum++;
        rate = sum / limit;
        System.out.format("%% klingons @ 0: %4.2f. Total: %2.0f \n", rate, rate * limit );

        rate = sum = 0.0;
        for(int i : data) if( i /10 % 10 == 1 ) sum++;
        rate = sum / limit;
        System.out.format("%% Bases    @ 1: %4.2f. Total Star Bases: %2.0f \n", rate, sum );

    }
}

/**
 * 
    318
    Quadrant K(3); B(1), S(8). 
     >     0 : 004.
     >     1 : 004.
     >     2 : 005.
     >     3 : 005.
     >     4 : 002.
     >     5 : 107.
     >     6 : 101.
     >     7 : 001.
     >     8 : 104.
     >     9 : 005.
     >    10 : 305.
     >    11 : 105.
     >    12 : 106.
     >    13 : 001.
     >    14 : 007.
     >    15 : 107.
     >    16 : 105.
     >    17 : 004.
     >    18 : 004.
     >    19 : 006.
     >    20 : 008.
     >    21 : 003.
     >    22 : 003.
     >    23 : 003.
     >    24 : 003.
     >    25 : 008.
     >    26 : 307.
     >    27 : 014.
     >    28 : 006.
     >    29 : 004.
     >    30 : 003.
     >    31 : 006.
     >    32 : 003.
     >    33 : 002.
     >    34 : 108.
     >    35 : 001.
     >    36 : 006.
     >    37 : 107.
     >    38 : 104.
     >    39 : 003.
     >    40 : 006.
     >    41 : 001.
     >    42 : 008.
     >    43 : 008.
     >    44 : 002.
     >    45 : 006.
     >    46 : 003.
     >    47 : 003.
     >    48 : 007.
     >    49 : 006.
     >    50 : 006.
     >    51 : 008.
     >    52 : 002.
     >    53 : 008.
     >    54 : 003.
     >    55 : 003.
     >    56 : 008.
     >    57 : 006.
     >    58 : 008.
     >    59 : 107.
     >    60 : 105.
     >    61 : 004.
     >    62 : 005.
     >    63 : 007.
    Statistics: 
    % klingons @ 3: 0.03. Total:  2 
    % klingons @ 2: 0.00. Total:  0 
    % klingons @ 1: 0.19. Total: 12 
    % klingons @ 0: 0.78. Total: 50 
    % Bases    @ 1: 0.02. Total Star Bases:  1 
 * 
 */