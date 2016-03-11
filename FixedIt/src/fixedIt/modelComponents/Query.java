package fixedIt.modelComponents;


public class Query {
	//term constants
	public static final int SPECIAL_SESSION_2016	=201590;
	public static final int SUMMER_II_2016			=201550;
	public static final int SUMMER_I_2016			=201540;
	public static final int MINIMESTER_2016			=201530;
	public static final int SPRING_2016				=201520;
	
	//level constants
	public static final String LEVEL_UNDERGRAD							="A";
	public static final String LEVEL_GRADUATE							="M";
	public static final String LEVEL_EVENING_AND_SATURDAY_UNDERGRAD		="E";
	
	//department constants
	public static final String	ANT_01	=	"ANT_01"	;
	public static final String	BEH_01	=	"BEH_01"	;
	public static final String	CJA_01	=	"CJA_01"	;
	public static final String	GER_01	=	"GER_01"	;
	public static final String	HSV_01	=	"HSV_01"	;
	public static final String	PSY_01	=	"PSY_01"	;
	public static final String	SOC_01	=	"SOC_01"	;
	public static final String	BIO_02	=	"BIO_02"	;
	public static final String	PMD_02	=	"PMD_02"	;
	public static final String	RT_02	=	"RT_02"		;
	public static final String	ACC_03	=	"ACC_03"	;
	public static final String	ECO_03	=	"ECO_03"	;
	public static final String	ENT_03	=	"ENT_03"	;
	public static final String	FIN_03	=	"FIN_03"	;
	public static final String	BUS_03	=	"BUS_03"	;
	public static final String	IFS_03	=	"IFS_03"	;
	public static final String	IBS_03	=	"IBS_03"	;
	public static final String	MGT_03	=	"MGT_03"	;
	public static final String	MKT_03	=	"MKT_03"	;
	public static final String	QBA_03	=	"QBA_03"	;
	public static final String	SCM_03	=	"SCM_03"	;
	public static final String	ART_07	=	"ART_07"	;
	public static final String	CM_07	=	"CM_07"		;
	public static final String	MUS_07	=	"MUS_07"	;
	public static final String	THE_07	=	"THE_07"	;
	public static final String	ECH_04	=	"ECH_04"	;
	public static final String	EDU_04	=	"EDU_04"	;
	public static final String	MLE_04	=	"MLE_04"	;
	public static final String	SE_04	=	"SE_04"		;
	public static final String	SPE_04	=	"SPE_04"	;
	public static final String	CS_12	=	"CS_12"		;
	public static final String	ECE_12	=	"ECE_12"	;
	public static final String	EGR_12	=	"EGR_12"	;
	public static final String	ME_12	=	"ME_12"		;
	public static final String	PHY_12	=	"PHY_12"	;
	public static final String	FLM_05	=	"FLM_05"	;
	public static final String	FCO_05	=	"FCO_05"	;
	public static final String	FRN_05	=	"FRN_05"	;
	public static final String	GRM_05	=	"GRM_05"	;
	public static final String	HUM_05	=	"HUM_05"	;
	public static final String	INT_05	=	"INT_05"	;
	public static final String	ITL_05	=	"ITL_05"	;
	public static final String	LAT_05	=	"LAT_05"	;
	public static final String	LIT_05	=	"LIT_05"	;
	public static final String	PHL_05	=	"PHL_05"	;
	public static final String	REL_05	=	"REL_05"	;
	public static final String	RUS_05	=	"RUS_05"	;
	public static final String	SPN_05	=	"SPN_05"	;
	public static final String	WRT_05	=	"WRT_05"	;
	public static final String	G_06	=	"G_06"	 	;
	public static final String	HIS_06	=	"HIS_06"	;
	public static final String	IA_06	=	"IA_06"		;
	public static final String	INT_06	=	"INT_06"	;
	public static final String	PS_06	=	"PS_06"		;
	public static final String	HSP_11	=	"HSP_11"	;
	public static final String	PE_11	=	"PE_11"		;
	public static final String	REC_11	=	"REC_11"	;
	public static final String	SPM_11	=	"SPM_11"	;
	public static final String	FYS_10	=	"FYS_10"	;
	public static final String	SES_10	=	"SES_10"	;
	public static final String	WGS_10	=	"WGS_10"	;
	public static final String	NUR_08	=	"NUR_08"	;
	public static final String	CHM_09	=	"CHM_09"	;
	public static final String	ESS_09	=	"ESS_09"	;
	public static final String	FCM_09	=	"FCM_09"	;
	public static final String	MAT_09	=	"MAT_09"	;
	public static final String	PSC_09	=	"PSC_09"	;
	public static final String	PHY_09	=	"PHY_09"	;

	
	private int term;
	private String level;
	private String dept;
	private String URL;
	
	/**
	 * Creates a query with the given parameters
	 * @param term term to query within
	 * @param level level to query within 
	 * @param dept department to query within
	 */
	public Query(int term, String level,String dept){
		this.term=term;
		this.level=level;
		this.dept=dept;
	}
	
	/**
	 * Initializes an empty query
	 */
	public Query(){
		term=-1;
		level=new String();
		dept=new String();
		URL=new String();
	}
	public int getTerm() {
		return term;
	}
	public void setTerm(int term) {
		this.term = term;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	
	/**
	 * Create the URL with the query parameters contained within
	 * this query object. Never needs to be and cannot be called directly.
	 * @return URL the URL to send query to
	 * @throws IllegalArgumentException if the query object has
	 * 			has not been fully/properly initialized.
	 */
	private String generateURL(){
		if(term>=0 && level!="" && dept!=""){
			URL="http://ycpweb.ycp.edu/schedule-of-classes/index.html?term="+term+"&stype="+level+"&dmode=D&dept="+dept;
		}
		else{
			throw new IllegalArgumentException("This query object has not been fully initialized or is missing query parameters.");
		}
		return URL;
	}
	
	/**
	 * Creates a Registrar object as a child of this Query object. This is the only
	 * method that should be directly called (besides setters and getters).
	 * @return Registrar a child Registrar of this Query
	 * @throws IllegalArgumentException if the Query has not been
	 * 			fully/properly initialized.
	 */
	public Registrar createRegistrar(){
		if(term>=0){
			if(level!=""){
				if(dept!=""){
					return new Registrar(generateURL());
				}
				else{
					throw new IllegalArgumentException("Department not set.");
				}
			}
			else{
				throw new IllegalArgumentException("Level and/or Department not set.");
			}
		}
		else{
			throw new IllegalArgumentException("Term and/or Level and/or Department not set.");
		}
	}
	@Override
	public String toString() {
		return "Query [term=" + term + ", level=" + level + ", dept=" + dept + ", URL=" + URL + "]";
	}
}
