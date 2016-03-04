package tests;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Model.Course;
import Model.Registrar;

public class RegistrarTest {
	Registrar r;
	ArrayList<String> depts;

	@Before
	public void setUp() {
		depts=new ArrayList<String>();
		depts.add("ANT_01");
		depts.add("BEH_01");
		depts.add("CJA_01");
		depts.add("GER_01");
		depts.add("HSV_01");
		depts.add("PSY_01");
		depts.add("SOC_01");
		depts.add("BIO_02");
		depts.add("PMD_02");
		depts.add("RT_02");
		depts.add("ACC_03");
		depts.add("ECO_03");
		depts.add("ENT_03");
		depts.add("FIN_03");
		depts.add("BUS_03");
		depts.add("IFS_03");
		depts.add("IBS_03");
		depts.add("MGT_03");
		depts.add("MKT_03");
		depts.add("QBA_03");
		depts.add("SCM_03");
		depts.add("ART_07");
		depts.add("CM_07");
		depts.add("MUS_07");
		depts.add("THE_07");
		depts.add("ECH_04");
		depts.add("EDU_04");
		depts.add("MLE_04");
		depts.add("SE_04");
		depts.add("SPE_04");
		depts.add("CS_12");
		depts.add("ECE_12");
		depts.add("EGR_12");
		depts.add("ME_12");
		depts.add("PHY_12");
		depts.add("FLM_05");
		depts.add("FCO_05");
		depts.add("FRN_05");
		depts.add("GRM_05");
		depts.add("HUM_05");
		depts.add("INT_05");
		depts.add("ITL_05");
		depts.add("LAT_05");
		depts.add("LIT_05");
		depts.add("PHL_05");
		depts.add("REL_05");
		depts.add("RUS_05");
		depts.add("SPN_05");
		depts.add("WRT_05");
		depts.add("G_06");
		depts.add("HIS_06");
		depts.add("IA_06");
		depts.add("INT_06");
		depts.add("PS_06");
		depts.add("HSP_11");
		depts.add("PE_11");
		depts.add("REC_11");
		depts.add("SPM_11");
		depts.add("FYS_10");
		depts.add("SES_10");
		depts.add("WGS_10");
		depts.add("NUR_08");
		depts.add("CHM_09");
		depts.add("ESS_09");
		depts.add("FCM_09");
		depts.add("MAT_09");
		depts.add("PSC_09");
		depts.add("PHY_09");
	}
	
	@Test
	public void testFetch() {
		for(String s : depts){
			r=new Registrar("http://ycpweb.ycp.edu/schedule-of-classes/index.html?term=201520" + "&stype=A&dmode=D&dept=" + s);
			try {
				for (Course c : r.fetch()) {
					System.out.println(c.toCSVLine());
				}
				System.out.println();
				System.out.println();
				System.out.println();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}