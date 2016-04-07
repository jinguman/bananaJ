package app.kit.com.daemon;

/**
 * Starter Loader Interface
 *
 */
public interface IBananaLoader {

	/**
	 * Loader Start
	 * @throws Exception
	 */
	public void startEngine() throws Exception;
	/**
	 * Loader Stop
	 * @throws Exception
	 */
	public void stopEngine() throws Exception;
}
