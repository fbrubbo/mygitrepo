package br.com.datamaio.envconfig.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

class ThreadedStreamHandler extends Thread
{
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final InputStream in;
    private boolean printoutput = false;
    private final StringBuilder output = new StringBuilder();

    ThreadedStreamHandler(InputStream in)
    {
        this(in, false);
    }

    ThreadedStreamHandler(InputStream in, boolean printoutput)
    {
        this.in = in;
        this.printoutput = printoutput;
    }

    @Override
    public void run()
    {
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while((line = br.readLine()) != null)
            {
                if(printoutput)
                {
                    LOGGER.info(line);
                }
                output.append(line + "\n");
            }
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            try
            {
                br.close();
            }
            catch(IOException e)
            {
                // ignore this one
            }
        }
    }

    public String getOutput()
    {
        String st = output.toString();
        if(st.contains("[sudo] password for"))
        {
            return "";
        }
        return st;
    }

    public void setPrintoutput(boolean printoutput)
    {
        this.printoutput = printoutput;
    }

}