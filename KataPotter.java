package CodeJam2008PracticeContest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Stack;

public class KataPotter {

	/**
	 * ���ﳵ��
	 * 
	 * @author Laoniu
	 * 
	 */

	static public class BookCart {
		ArrayList<Book> books;
		int minSelectPackageIndex;
		int totalBooks;

		public BookCart() {
			books = new ArrayList<Book>();
		}

		public void addBook(Book book) {
			totalBooks += book.count;
			books.add(book);
		}

		/**
		 * ȡ��һ����
		 * 
		 * @param book
		 * @return
		 */
		boolean pickOneBook(Book book) {
			for (Book bk : books) {
				if (bk.name.equals(book.name)) {
					bk.count -= 1;
					totalBooks -= 1;
					if (bk.count == 0) {
						books.remove(bk);
					}

					return true;
				}
			}
			return false;

		}

		/**
		 * Ϊȥ��д��һ��һ��ɾ������ķ���
		 * 
		 * @param index
		 * @return
		 */
		boolean removeFromIndex(int index) {
			if (index >= books.size()) {
				return false;
			} else {
				for (int i = books.size() - 1; i >= index; i--) {
					totalBooks -= books.get(i).count;
					books.remove(i);
				}
				return true;
			}
		}

		public BookCart copy() {
			BookCart ret = new BookCart();
			for (Book bk : this.books) {
				ret.addBook(bk.copy());
			}
			return ret;
		}
	}

	/**
	 * �鼮��
	 * 
	 * @author Laoniu
	 * 
	 */
	static public class Book {
		String name;
		int count;

		public Book(String in_name, int in_count) {
			name = new String(in_name);
			count = in_count;
		}

		public Book copy() {
			return new Book(this.name, count);
		}

	}

	/**
	 * �鼮���(���Żݹʴ��)
	 * 
	 * @author Laoniu
	 * 
	 */
	static public class BookPackage {
		ArrayList<Book> books;

		public BookPackage() {
			books = new ArrayList<KataPotter.Book>();
		}

		public BookPackage copy() {
			BookPackage pkg = new BookPackage();

			for (Book book : this.books) {
				pkg.add(book.copy());
			}
			return pkg;
		}

		public void add(Book book) {
			books.add(book);
		}
	}

	/**
	 * ������(������;)
	 * @author Laoniu
	 * 
	 *
	 */
	static public class PackegeCombo {

		ArrayList<BookPackage> BookPackages;
		int totalPrice;

		public PackegeCombo() {
			BookPackages = new ArrayList<KataPotter.BookPackage>();
		}

		public void add(BookPackage pkg) {
			BookPackages.add(pkg);
		}

		public PackegeCombo copy() {
			PackegeCombo pkgcmb = new PackegeCombo();
			for (BookPackage pkg : BookPackages) {
				pkgcmb.add(pkg.copy());
			}
			return pkgcmb;
		}
	}

	/**
	 * �Żݲ���
	 * 
	 * @author Laoniu
	 * 
	 */
	static public class Strategy {
		int bookCount;
		int discountRate;

		public Strategy(int count, int in_rate) {
			bookCount = count;
			discountRate = in_rate;
		}

		int getStrategyPrice() {
			return bookCount * discountRate;
		}

		public Strategy copy() {
			return new Strategy(this.bookCount, this.discountRate);

		}
	}
	
	

	/**
	 * �Ż���� �����һ�����ﳵ�ж����Ż����
	 * 
	 * @author Laoniu
	 * 
	 */
	static public class StategyCombo {
		ArrayList<Strategy> strategies;// �Ż����
		int totalPrice;

		public StategyCombo() {
			strategies = new ArrayList<KataPotter.Strategy>();
			totalPrice = 0;
		}

		public void add(Strategy stategy) {
			// TODO Auto-generated method stub
			strategies.add(stategy);
			totalPrice += stategy.getStrategyPrice();
		}
	}

	public KataPotter() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * �ҳ����п��ܵ�ѡ�����
	 * @param cart
	 * @return
	 */
	public ArrayList<PackegeCombo> genAllPickAblePackage(BookCart cart)
	{
		ArrayList<PackegeCombo> pkgcmbs=new ArrayList<KataPotter.PackegeCombo>();
		for(int i=1;i<=cart.books.size();i++)
		{
			BookCart cyc_cart=cart.copy();
			ArrayList<BookPackage> pkgs=pickablePackegeFromBookCart(cyc_cart, i);
			PackegeCombo pkgcmb=new PackegeCombo();
			pkgcmb.BookPackages=pkgs;
			pkgcmbs.add(pkgcmb);
		}
		return pkgcmbs;
	}
	
	/**
	 * ���Ż���Ͽ���ʹ�õĴ����ʽ
	 * 
	 * @return
	 */
	public ArrayList<PackegeCombo> pickAblePakegeCombo(
			BookCart BookCart, StategyCombo strategyCombo,ArrayList<PackegeCombo> pkgcmbs) {
		
		ArrayList<BookCart> BookCarts = new ArrayList<BookCart>();
		ArrayList<PackegeCombo> pkgcombos = new ArrayList<PackegeCombo>();
		BookCarts.add(BookCart.copy());
		int []minsel=new int[BookCart.books.size()];
		
		
		for (int i = 0; i < strategyCombo.strategies.size(); i++) {
			ArrayList<BookCart> newBookCarts = new ArrayList<BookCart>();
			ArrayList<PackegeCombo> newPkgCombos = new ArrayList<PackegeCombo>();
			int curPackageIndex= strategyCombo.strategies.get(i).bookCount-1;
			
			for (int j = BookCarts.size() - 1; j >= 0; j--) {
				 
				ArrayList<BookPackage> pkgs=pkgcmbs.get(curPackageIndex).BookPackages;
				for(int k=minsel[curPackageIndex];k<pkgs.size();k++)
				{
					BookCart newCart = pickPackegeFromBookCart(BookCarts.get(j), pkgs.get(k));
					
					if(newCart!=null)
					{
						
						newBookCarts.add(newCart);
						PackegeCombo pkgcmb = (i == 0) ? new PackegeCombo()
						: pkgcombos.get(j).copy();
						minsel[curPackageIndex]=i;
						BookPackage pkg=pkgs.get(k).copy();
						pkgcmb.add(pkg);
						pkgcmb.totalPrice += strategyCombo.totalPrice;
						newPkgCombos.add(pkgcmb);
					}
				}
				
				
			}
			// ���ﳵ�޷��ٲ����� �Ƴ�����
			if (newBookCarts.size() == 0) {
				return null;// ��ʾû�н��
			}
			BookCarts = newBookCarts;
			pkgcombos = newPkgCombos;
			
		}
		return pkgcombos;
	}

	/**
	 * ���Ż���Ͽ���ʹ�õĴ����ʽ
	 * 
	 * @return
	 */
	/*
	public ArrayList<PackegeCombo> pickAblePakegeComboWithStategyComboFromBookCart(
			BookCart BookCart, StategyCombo strategyCombo) {

		ArrayList<BookCart> BookCarts = new ArrayList<BookCart>();
		ArrayList<PackegeCombo> pkgcombos = new ArrayList<PackegeCombo>();
		BookCarts.add(BookCart.copy());

		for (int i = 0; i < strategyCombo.strategies.size(); i++) {
			ArrayList<BookCart> newBookCarts = new ArrayList<BookCart>();
			ArrayList<PackegeCombo> newPkgCombos = new ArrayList<PackegeCombo>();
			for (int j = BookCarts.size() - 1; j >= 0; j--) {
				ArrayList<BookPackage> pkgs = pickablePackegeFromBookCart(
						BookCarts.get(j),
						strategyCombo.strategies.get(i).bookCount);
				// ����û�н���ķ�֧
				if (pkgs == null || pkgs.size() == 0) {
					BookCarts.remove(j);
				} else {
					// BookCart����Ϊpkgs����֧ newPkgCombos��BookCartsͬʱ����
					for (int k = 0; k < pkgs.size(); k++) {
						// �ӷ�֧��Ӧ��
						BookCart newCart = pickPackegeFromBookCart(BookCarts
								.get(j).copy(), pkgs.get(k));

						if (newCart == null) {
							continue;
						}
						newBookCarts.add(newCart);
						PackegeCombo pkgcmb = (i == 0) ? new PackegeCombo()
								: pkgcombos.get(j).copy();
						;
						pkgcmb.add(pkgs.get(k));
						pkgcmb.totalPrice = strategyCombo.totalPrice;
						newPkgCombos.add(pkgcmb);

					}
				}

			}
			// ���ﳵ�޷��ٲ����� �Ƴ�����
			if (newBookCarts.size() == 0) {
				return null;// ��ʾû�н��
			}
			BookCarts = newBookCarts;
			pkgcombos = newPkgCombos;
		}
		return pkgcombos;
	}
	*/

			

	/**
	 * ���涨�����ɴӹ��ﳵ��ȡ�����鼮�Żݰ�
	 * 
	 * @param BookCart
	 * @param BookPackageCount(�м�����)
	 * @return
	 */
	public ArrayList<BookPackage> pickablePackegeFromBookCart(
			BookCart BookCart, int BookPackageCount) {
		ArrayList<BookCart> BookCarts = new ArrayList<BookCart>();
		BookCarts.add(BookCart.copy());
		ArrayList<BookPackage> pkgs = new ArrayList<BookPackage>();
		if (BookCart.books.size() < BookPackageCount)//
		{
			return null;
		}
		for (int i = 0; i < BookPackageCount; i++) {
			ArrayList<BookCart> newBookCarts = new ArrayList<BookCart>();
			ArrayList<BookPackage> newpkgs = new ArrayList<BookPackage>();
			for (int j = BookCarts.size() - 1; j >= 0; j--) {
				// ʣ����Ҫ��ѡ��������ڹ��ﳵ���ʣ���������˳�
				if (BookPackageCount - i > BookCarts.get(j).books.size()) {
					BookCarts.remove(j);
				} else {

					for (int k = 0; k < BookCarts.get(j).books.size(); k++) {
						BookCart newBookCart = BookCarts.get(j).copy();
						// Ϊȥ�� ȡ��һ��Ԫ��ʱ�����֮���Ԫ��ȫ��ȡ��
						newBookCart.removeFromIndex(k);
						BookPackage pkg = (i == 0) ? (new BookPackage()) : pkgs
								.get(j).copy();
						Book book = BookCarts.get(j).books.get(k).copy();
						book.count = 1;
						pkg.add(book);
						newpkgs.add(pkg);
						newBookCarts.add(newBookCart);
					}
				}

			}
			// ���ﳵ�޷��ٲ����� �Ƴ�����
			if (newBookCarts.size() == 0) {
				return null;// ��ʾû�н��
			}
			BookCarts = newBookCarts;
			pkgs = newpkgs;
		}

		return pkgs;
	}

	/**
	 * �ӹ��ﳵ��ȡ��
	 * 
	 * @param BookCart
	 * @param pkg
	 * @return
	 */
	public BookCart pickPackegeFromBookCart(BookCart in_BookCart,
			BookPackage pkg) {
		BookCart bkCart = in_BookCart.copy();
		for (Book book : pkg.books) {
			if (false == bkCart.pickOneBook(book)) {
				return null;
			}
		}

		return bkCart;
	}
	
	

	/**
	 * @param BookCart
	 * @return
	 */
	public ArrayList<StategyCombo> allUseAbleStrategyComboFromBookCart(
			BookCart BookCart, ArrayList<Strategy> avalibleStrategies) {
		int[] stategyCounts = new int[avalibleStrategies.size()];
		int[] stategyMax = new int[avalibleStrategies.size()];

		ArrayList<StategyCombo> stategyCombos = new ArrayList<StategyCombo>();
		// ȷ��ÿ���Żݲ���ʹ�õļ�ֵ
		for (int i = 0; i < avalibleStrategies.size(); i++) {
			stategyMax[i] = BookCart.totalBooks
					/ avalibleStrategies.get(i).bookCount;
		}
		int stategySize = avalibleStrategies.size();
		while (true) {
			// ��������(��λ����)
			for (int i = 0; i < stategySize - 1; i++) {

				if (stategyCounts[i] > stategyMax[i]) {
					stategyCounts[i] = 0;
					stategyCounts[i + 1] += 1;
				}
			}
			// ѭ���˳���(���һ��������Ϊ������)
			if (stategyCounts[stategySize - 1] > stategyMax[stategySize - 1]) {
				break;
			}
			int totalBook = 0;

			for (int i = 0; i < stategySize; i++) {
				totalBook += avalibleStrategies.get(i).bookCount
						* stategyCounts[i];
			}
			// ��������Ϊ8��Ϊ���ܵ��Ż����
			if (totalBook == BookCart.totalBooks) {
				StategyCombo stategyCombo = new StategyCombo();
				for (int i = 0; i < avalibleStrategies.size(); i++) {
					for (int j = 0; j < stategyCounts[i]; j++) {
						stategyCombo.add(avalibleStrategies.get(i).copy());
					}
				}
				stategyCombos.add(stategyCombo);
			}

			stategyCounts[0]++;
		}
		return stategyCombos;
	}

	/**
	 * ���Ż���Ͻ�������
	 * 
	 * @param combos
	 * @return
	 */
	public ArrayList<StategyCombo> sortStrategyCombos(
			ArrayList<StategyCombo> combos) {
		Collections.sort(combos, new Comparator<StategyCombo>() {

			@Override
			public int compare(StategyCombo o1, StategyCombo o2) {
				// TODO Auto-generated method stub
				if (o1.totalPrice > o2.totalPrice) {
					return 1;
				}
				if (o1.totalPrice < o2.totalPrice) {
					return -1;
				}
				return 0;
			}
		});
		return combos;
	}

	/**
	 * ������ŻݵĴ����ʽ(���ܲ�ֹһ��)
	 * 
	 * @return
	 */
	public ArrayList<PackegeCombo> getTheCheapestPurchasePackegs(
			BookCart cart, ArrayList<Strategy> avalibleStrategies) {
		// ������п��ܵ��Ż����
		ArrayList<StategyCombo> stragycombos = allUseAbleStrategyComboFromBookCart(
				cart, avalibleStrategies);
		// ���Ż���Ͻ�������
		stragycombos = sortStrategyCombos(stragycombos);
		ArrayList<PackegeCombo> pregen_pkgcmb=genAllPickAblePackage(cart);

		ArrayList<PackegeCombo> ret_packegeCombos = new ArrayList<PackegeCombo>();
		for (int i = 0; i < stragycombos.size(); i++) {
			// �鿴���Ż�����Ƿ���Դ���ɹ�
			ArrayList<PackegeCombo> packegeCombos_i = pickAblePakegeCombo(
					cart, stragycombos.get(i),pregen_pkgcmb);
			if (packegeCombos_i != null) {
				ret_packegeCombos.addAll(packegeCombos_i);
			}
			// ���ڲ����ǰ��۸������ �����һ�����Եļ۸���ڱ������ұ������Ѿ��ҵ���� ��ôѭ������
			if (i < stragycombos.size() - 1
					&& ret_packegeCombos.size() > 0
					&& stragycombos.get(i + 1).totalPrice > stragycombos.get(i).totalPrice) {
				break;
			}

		}
		return ret_packegeCombos;
	}

	/**
	 * ������ŻݵĹ���۸�
	 * 
	 * @param BookCart
	 * @param avalibleStrategies
	 * @return
	 */
	public int returnTheCheepestPurchasePrice(BookCart BookCart,
			ArrayList<Strategy> avalibleStrategies) {
		ArrayList<PackegeCombo> packegeCombos = getTheCheapestPurchasePackegs(
				BookCart, avalibleStrategies);
		return getPricebyPackageCombo(packegeCombos);
	}

	public int getPricebyPackageCombo(ArrayList<PackegeCombo> packegeCombos) {
		if (packegeCombos != null && packegeCombos.size() > 0) {
			return packegeCombos.get(0).totalPrice;
		}
		return 0;
	}

}
