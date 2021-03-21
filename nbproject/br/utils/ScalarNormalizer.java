package utils;

public class ScalarNormalizer {
    private float minValue;
    private float maxValue;
    private float initInterval;
    private float endInterval;

    public ScalarNormalizer(float _minValue, float _maxValue, float _initInterval, float _endInterval)
    {
        this.minValue = _minValue;
        this.maxValue = _maxValue;
        this.initInterval = _initInterval;
        this.endInterval = _endInterval;
    }

    public float run(float value)
    {
        float normalizedValue = ((value - this.minValue)/(this.maxValue - this.minValue)) * (this.endInterval - this.initInterval) + this.initInterval;
        return normalizedValue;
    }
}
