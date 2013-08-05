package CodeJam2008PracticeContest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Stack;

public class KataPotter {

	/**
	 * 购物车类
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
		 * 取出一本书
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
		 * 为去重写了一个一次删除多个的方法
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
	 * 书籍类
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
	 * 书籍打包(可优惠故打包)
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
	 * 打包组合(多种用途)
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
	 * 优惠策略
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
	 * 优惠组合 针对于一个购物车有多种优惠组合
	 * 
	 * @author Laoniu
	 * 
	 */
	static public class StategyCombo {
		ArrayList<Strategy> strategies;// 优惠组合
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
	 * 找出所有可能的选择组合
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
	 * 此优惠组合可以使用的打包方式
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
			// 购物车无法再操作了 推出程序
			if (newBookCarts.size() == 0) {
				return null;// 表示没有结果
			}
			BookCarts = newBookCarts;
			pkgcombos = newPkgCombos;
			
		}
		return pkgcombos;
	}

	/**
	 * 此优惠组合可以使用的打包方式
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
				// 剪除没有结果的分支
				if (pkgs == null || pkgs.size() == 0) {
					BookCarts.remove(j);
				} else {
					// BookCart分裂为pkgs个分支 newPkgCombos和BookCarts同时分裂
					for (int k = 0; k < pkgs.size(); k++) {
						// 从分支对应的
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
			// 购物车无法再操作了 推出程序
			if (newBookCarts.size() == 0) {
				return null;// 表示没有结果
			}
			BookCarts = newBookCarts;
			pkgcombos = newPkgCombos;
		}
		return pkgcombos;
	}
	*/

			

	/**
	 * 按规定数量可从购物车中取出的书籍优惠包
	 * 
	 * @param BookCart
	 * @param BookPackageCount(有几本书)
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
				// 剩余需要挑选的种类大于购物车里的剩余种类则退出
				if (BookPackageCount - i > BookCarts.get(j).books.size()) {
					BookCarts.remove(j);
				} else {

					for (int k = 0; k < BookCarts.get(j).books.size(); k++) {
						BookCart newBookCart = BookCarts.get(j).copy();
						// 为去重 取出一个元素时会把它之后的元素全部取出
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
			// 购物车无法再操作了 推出程序
			if (newBookCarts.size() == 0) {
				return null;// 表示没有结果
			}
			BookCarts = newBookCarts;
			pkgs = newpkgs;
		}

		return pkgs;
	}

	/**
	 * 从购物车中取出
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
		// 确定每种优惠策略使用的极值
		for (int i = 0; i < avalibleStrategies.size(); i++) {
			stategyMax[i] = BookCart.totalBooks
					/ avalibleStrategies.get(i).bookCount;
		}
		int stategySize = avalibleStrategies.size();
		while (true) {
			// 整理数据(进位操作)
			for (int i = 0; i < stategySize - 1; i++) {

				if (stategyCounts[i] > stategyMax[i]) {
					stategyCounts[i] = 0;
					stategyCounts[i + 1] += 1;
				}
			}
			// 循环退出点(最后一个数加满为结束点)
			if (stategyCounts[stategySize - 1] > stategyMax[stategySize - 1]) {
				break;
			}
			int totalBook = 0;

			for (int i = 0; i < stategySize; i++) {
				totalBook += avalibleStrategies.get(i).bookCount
						* stategyCounts[i];
			}
			// 满足总数为8即为可能的优惠组合
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
	 * 对优惠组合进行排序
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
	 * 获得最优惠的打包方式(可能不止一种)
	 * 
	 * @return
	 */
	public ArrayList<PackegeCombo> getTheCheapestPurchasePackegs(
			BookCart cart, ArrayList<Strategy> avalibleStrategies) {
		// 获得所有可能的优惠组合
		ArrayList<StategyCombo> stragycombos = allUseAbleStrategyComboFromBookCart(
				cart, avalibleStrategies);
		// 对优惠组合进行排序
		stragycombos = sortStrategyCombos(stragycombos);
		ArrayList<PackegeCombo> pregen_pkgcmb=genAllPickAblePackage(cart);

		ArrayList<PackegeCombo> ret_packegeCombos = new ArrayList<PackegeCombo>();
		for (int i = 0; i < stragycombos.size(); i++) {
			// 查看此优惠组合是否可以打包成功
			ArrayList<PackegeCombo> packegeCombos_i = pickAblePakegeCombo(
					cart, stragycombos.get(i),pregen_pkgcmb);
			if (packegeCombos_i != null) {
				ret_packegeCombos.addAll(packegeCombos_i);
			}
			// 由于策略是按价格排序的 如果下一个策略的价格大于本策略且本策略已经找到结果 那么循环结束
			if (i < stragycombos.size() - 1
					&& ret_packegeCombos.size() > 0
					&& stragycombos.get(i + 1).totalPrice > stragycombos.get(i).totalPrice) {
				break;
			}

		}
		return ret_packegeCombos;
	}

	/**
	 * 获得最优惠的购书价格
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
