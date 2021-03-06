package project.senior.app.pormmo;

import java.util.ArrayList;

/**
 *
 * @author Shelby Copeland
 */
public class SequenceOrder
{

  private String order;
  private ArrayList<BrightnessSaturationSettings> bss;
  public final int GS = 1,
          MEDIAN = 2,
          MEAN = 3,
          GAUSSIAN = 4,
          P16B = 5,
          REMOVE_POST = 6,
          REMOVE_PRE = 7,
          BRIGHTNESS_SAT = 8;

  public SequenceOrder()
  {
    order = "";
    bss = new ArrayList<>();
  }

  /**
   * For a list of actions, call your SequenceOrder object for a list of final
   * [const] ints.
   *
   * @param action The action used.
   */
  public void AddAction(int action)
  {
    order += action + "";
  }

  public void AddAction(int action, BrightnessSaturationSettings bs)
  {
    order += action + "";
    bss.add(bs);
  }

  private void TEST()
  {
    System.out.println(order);
    int[] test = GetOrder();

    for (int i : test)
    {
      System.out.println(i);
    }
  }

  public void Reset()
  {
    //TEST();
    order = "";
  }

  /**
   * @return int[] containing the actions in order, starts at 0
   */
  public int[] GetOrder()
  {
    int[] ret = new int[order.length()];

    int i = 0;
    for (char c : order.toCharArray())
    {
      ret[i++] = Integer.parseInt(c + "");
    }

    return ret;
  }

  /**
   * Applies the order to the current image.
   *
   * @param bw The Image (no green removed yet).
   */
  public void ApplySequence(BufferedWrapper bw, float ratio)
  {
    int which = 0;
    int[] order = GetOrder();
    GSR gsr = new GSR();

    for (int i : order)
    {
      switch (i)
      {
        case GS: //grayscale
          Filters.GrayScaleStatic(bw);
          break;
//==============================================================================
        case MEAN:
          Filters.MeanBlurStatic(bw);
          break;
//============================================================================== 
        case MEDIAN:
          Filters.MedianBlurStatic(bw);
          break;
//==============================================================================
        case GAUSSIAN:
          Filters.GaussianBlurStatic(bw);
          break;
//============================================================================== 
        case P16B:
          Filters.BitFilter(bw, Filters.SixteenBit);
          break;
//==============================================================================
        case REMOVE_POST:
          gsr.RemoveGreen_2(bw, ratio);
          break;
//============================================================================== 
        case REMOVE_PRE:
          gsr.RemoveGreen_3(bw, ratio);
          break;
//============================================================================== 
        case BRIGHTNESS_SAT:
          Brightness.AdjustBrightness(bw, (int) (bss.get(which)).getSettings()[0]);
          Saturation.AdjustSaturation(bw, (int) (bss.get(which)).getSettings()[1]);
          which++;
          break;
      }
    }//:end foreach
  }
}
