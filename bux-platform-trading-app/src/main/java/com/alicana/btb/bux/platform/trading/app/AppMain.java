package com.alicana.btb.bux.platform.trading.app;

import com.alicana.btb.bux.platform.trading.app.exception.TradingBotException;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Just reads program arguments and fire new bot instance.
 */
public class AppMain {

  private static final Logger log = LoggerFactory.getLogger(AppMain.class);

  private static Function<String[], Boolean> argsValidator =
      args -> args == null || args.length == 0 || args[0] == null || args[0].isBlank();

  /**
   * Entry point.
   *
   * @param args Single string argument in JSON format.
   */
  public static void main(String[] args) {

    if (argsValidator.apply(args)) {
      log.error(getMissingArgumentErrMsg());
      throw new TradingBotException(getMissingArgumentErrMsg());
    }

    log.info("Creating bot instance...");
    BasicTradingBot bot = BasicTradingBot.create(args[0]);
    try {
      bot.run();
    } catch (Exception e) {
      log.error("Execution failed! ", e);
    }
  }

  private static String getMissingArgumentErrMsg() {
    return """
        Missing trading bot arguments!
        E.g. user input:
        {"productId":"sbXYZ","buyPrice": 17.203, "upperSellLimit": 20.000,"lowerSellLimit":15.000}
        """;
  }

}
