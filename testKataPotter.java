package CodeJam2008PracticeContest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import CodeJam2008PracticeContest.KataPotter.Book;
import CodeJam2008PracticeContest.KataPotter.BookCart;
import CodeJam2008PracticeContest.KataPotter.BookPackage;
import CodeJam2008PracticeContest.KataPotter.PackegeCombo;
import CodeJam2008PracticeContest.KataPotter.StategyCombo;
import CodeJam2008PracticeContest.KataPotter.Strategy;

public class testKataPotter {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	public BookCart getTestCart1() {
		BookCart cart = new BookCart();
		cart.addBook(new Book("A", 3));
		cart.addBook(new Book("B", 3));
		cart.addBook(new Book("C", 2));
		cart.addBook(new Book("D", 1));
		cart.addBook(new Book("E", 1));
		return cart;
	}
	
	public BookCart getTestCart4() {
		BookCart cart = new BookCart();
		cart.addBook(new Book("A", 1));
		cart.addBook(new Book("B", 1));
		cart.addBook(new Book("C", 1));
		cart.addBook(new Book("D", 1));
		cart.addBook(new Book("E", 1));
		return cart;
	}
	
	public BookCart getTestCart2() {
		BookCart cart = new BookCart();
		cart.addBook(new Book("A", 2));
		cart.addBook(new Book("B", 3));
		cart.addBook(new Book("C", 1));
		cart.addBook(new Book("D", 1));
		cart.addBook(new Book("E", 1));
		return cart;
	}
	
	public BookCart getTestCart3() {
		BookCart cart = new BookCart();
		cart.addBook(new Book("A", 6));
		cart.addBook(new Book("B", 6));
		cart.addBook(new Book("C", 4));
		cart.addBook(new Book("D", 2));
		cart.addBook(new Book("E", 2));
		return cart;
	}
	
	

	public ArrayList<Strategy> getTestStageries() {
		ArrayList<Strategy> stragies = new ArrayList<KataPotter.Strategy>();
		stragies.add(new Strategy(1, 100));
		stragies.add(new Strategy(2, 95));
		stragies.add(new Strategy(3, 90));
		stragies.add(new Strategy(4, 80));
		stragies.add(new Strategy(5, 75));
		return stragies;
	}
	public void PrintCart(BookCart cart)
	{
		String ln="Cart:";
		for(Book bk:cart.books)
		{
			ln=ln+bk.name+","+bk.count+":";
		}
		System.out.println(ln);
	}
	public void printStagertCombos(ArrayList<StategyCombo> stategyCombos) {
		System.out.println("No.\ttotalprice\t stategray");
		for (int i = 0; i < stategyCombos.size(); i++) {
			String prtln = " ";
			int price = 0;
			for (Strategy stg : stategyCombos.get(i).strategies) {
				prtln = prtln + stg.bookCount + " ";
				price += stg.getStrategyPrice();
			}
			System.out.println(i + "\t:" + price * 8 + "\t " + prtln);
		}
	}
	
	public void printPackages(ArrayList<BookPackage> pkgs) {
		System.out.println("printPkgs");
		for(int j=0;j<pkgs.size();j++)
		{
			String prtln ="pkg"+j+":";
			for(Book bk:pkgs.get(j).books)
			{
				prtln=prtln+bk.name+" ";
			}
			System.out.println(prtln);
		}
	}

	public void printPackageCombos(ArrayList<PackegeCombo> pkgcmbs) {
		for (int i = 0; i < pkgcmbs.size(); i++) {
			System.out.println("packageCombo"+i);
			
			
			for(int j=0;j<pkgcmbs.get(i).BookPackages.size();j++)
			{
				String prtln ="pkg"+j+":";
				for(Book bk:pkgcmbs.get(i).BookPackages.get(j).books)
				{
					prtln=prtln+bk.name+" ";
				}
				System.out.println(prtln);
			}
		}
	}
	@Test
	public void testKataPotter() {

		KataPotter kataPotter = new KataPotter();
		BookCart cart = getTestCart1();
		PrintCart(cart);
		ArrayList<Strategy> stragies = getTestStageries();
		ArrayList<PackegeCombo> pkgcmbs = kataPotter
				.getTheCheapestPurchasePackegs(cart, stragies);
		assertTrue(pkgcmbs != null && pkgcmbs.size() != 0);
		printPackageCombos(pkgcmbs);
	}

	@Test
	public void testStatgeryBase() {

		KataPotter kataPotter = new KataPotter();
		BookCart cart = getTestCart1();
		ArrayList<Strategy> stragies = getTestStageries();
		ArrayList<StategyCombo> stategyCombos = kataPotter
				.allUseAbleStrategyComboFromBookCart(cart, stragies);
		assertNotNull(stategyCombos);
		System.out.println("排序");
		stategyCombos = kataPotter.sortStrategyCombos(stategyCombos);
		printStagertCombos(stategyCombos);

	}
	
	
	/**
	 * 第二套测试数据
	 */
	@Test
	public void testKataPotter2() {
		
		KataPotter kataPotter = new KataPotter();
		BookCart cart = getTestCart2();
		PrintCart(cart);
		ArrayList<Strategy> stragies = getTestStageries();
		ArrayList<PackegeCombo> pkgcmbs = kataPotter
				.getTheCheapestPurchasePackegs(cart, stragies);
		assertTrue(pkgcmbs != null && pkgcmbs.size() != 0);
		printPackageCombos(pkgcmbs);
		assertEquals(pkgcmbs.get(0).totalPrice*8, 5320);
	}
	
	/**
	 * 第二套测试数据
	 */
	@Test
	public void testKataPotter3() {
		
		KataPotter kataPotter = new KataPotter();
		BookCart cart = getTestCart3();
		PrintCart(cart);
		ArrayList<Strategy> stragies = getTestStageries();
		ArrayList<PackegeCombo> pkgcmbs = kataPotter
				.getTheCheapestPurchasePackegs(cart, stragies);
		assertTrue(pkgcmbs != null && pkgcmbs.size() != 0);
		printPackageCombos(pkgcmbs);
		
		ArrayList<StategyCombo> stategyCombos = kataPotter
				.allUseAbleStrategyComboFromBookCart(cart, stragies);
		assertNotNull(stategyCombos);
		System.out.println("排序");
		stategyCombos = kataPotter.sortStrategyCombos(stategyCombos);
		printStagertCombos(stategyCombos);
		
	}
	
	@Test
	public void testPackagePick() {
		KataPotter kataPotter = new KataPotter();
		BookCart cart = getTestCart2();
		PrintCart(cart);
		ArrayList<BookPackage> pkgs= kataPotter.pickablePackegeFromBookCart(cart, 2);
		printPackages(pkgs);
		cart.pickOneBook(new Book("D", 1));
		PrintCart(cart);
		pkgs= kataPotter.pickablePackegeFromBookCart(cart, 2);
		printPackages(pkgs);
		
	}
	@Test
	public void testStatgeryBase2() {

		KataPotter kataPotter = new KataPotter();
		BookCart cart = getTestCart2();
		ArrayList<Strategy> stragies = getTestStageries();
		ArrayList<StategyCombo> stategyCombos = kataPotter
				.allUseAbleStrategyComboFromBookCart(cart, stragies);
		assertNotNull(stategyCombos);
		System.out.println("排序");
		stategyCombos = kataPotter.sortStrategyCombos(stategyCombos);
		printStagertCombos(stategyCombos);

	}

	@Test
	public void testCartBaseOP() {

		assertTrue("Not yet implemented", true);

		KataPotter kataPotter = new KataPotter();
		BookCart cart = getTestCart1();
		assertEquals(cart.totalBooks, 10);
		ArrayList<BookPackage> pkgs = kataPotter.pickablePackegeFromBookCart(
				cart, 5);
		assertEquals(pkgs.size(), 1);
		cart = kataPotter.pickPackegeFromBookCart(cart, pkgs.get(0));
		assertEquals(5, cart.totalBooks);
		pkgs = kataPotter.pickablePackegeFromBookCart(cart, 5);
		assertTrue(pkgs == null || pkgs.size() == 0);

	}
	@Test 
	public void testGenAllPackage()
	{
		KataPotter kataPotter = new KataPotter();
		BookCart cart = getTestCart4();
		ArrayList<BookPackage> pkgs = kataPotter.pickablePackegeFromBookCart(
				cart, 5);
		printPackages(pkgs);
		 pkgs = kataPotter.pickablePackegeFromBookCart(
				cart, 4);
		printPackages(pkgs);
		pkgs = kataPotter.pickablePackegeFromBookCart(
				cart, 3);
		printPackages(pkgs);
		pkgs = kataPotter.pickablePackegeFromBookCart(
				cart, 2);
		printPackages(pkgs);
		pkgs = kataPotter.pickablePackegeFromBookCart(
				cart, 1);
		printPackages(pkgs);
		
	}

}
