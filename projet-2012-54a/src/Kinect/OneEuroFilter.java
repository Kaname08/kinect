package Kinect;

/**
 * Implementation du filtre d'un euro.
 */
public class OneEuroFilter
{
    private double sampleRate;
    private double minFreqCut;
    private double beta;
    private double dFreqCut;
    private LowPassFilter x;
    private LowPassFilter dx;
    private LowPassFilter y;
    private LowPassFilter dy;

    /**
     * Initialise le filtre.
     * @param sampleRate temps entre deux donn�es
     * @param minFreqCut fr�quence de coupure minimale
     * @param beta facteur de contribution de la d�riv�e
     * @param dFreqCut fr�quence de coupure minimale de la d�riv�e
     */
    public OneEuroFilter(double sampleRate, double minFreqCut, double beta, double dFreqCut)
    {
        this.sampleRate = sampleRate;
        this.minFreqCut = minFreqCut;
        this.beta = beta;
        this.dFreqCut = dFreqCut;

        x = new LowPassFilter();
        dx = new LowPassFilter();

        y = new LowPassFilter();
        dy = new LowPassFilter();
    }

    /**
     * Retourne la valeur alpha pour le filtre passe bas.
     * @param freqCut fr�quence de coupure d�sir�e
     * @return la valeur alpha pour le filtre passe bas
     */
    public double getAlpha(double freqCut)
    {
        double tau = 1/(2*Math.PI*freqCut);
        return 1/(1 + tau/sampleRate);
    }

    /**
     * Retourne la valeur filtr�e.
     * @param value valeur a filtrer
     * @param v
     * @param dv
     * @return valeur filtr�e
     */
    public double filter(double value, LowPassFilter v, LowPassFilter dv)
    {
        double dvalue = v.isInitialized() ? (value - v.lastRawValue()) / sampleRate : 0.0;
        double edvalue = dv.filter(dvalue, getAlpha(dFreqCut));

        return v.filter(value, getAlpha(minFreqCut + beta * Math.abs(edvalue)));
    }

    /**
     * Retourne les valeurs filtr�e.
     * @param value1 premiere valeur a filtrer
     * @param value2 deuxi�me valeur a filtrer
     * @return valeurs filtr�es
     */
    public double[] filter(double value1, double value2)
    {
    	double[] result = { filter(value1, x, dx), filter(value2, y, dy) };
    	return result;
    }
}