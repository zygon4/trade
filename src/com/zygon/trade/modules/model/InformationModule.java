/**
 *
 */
package com.zygon.trade.modules.model;

import com.zygon.trade.Module;
import com.zygon.trade.market.model.indication.InformationManager;
import com.zygon.trade.modules.data.DataModule;
import com.zygon.trade.strategy.TradeAgent;
import com.zygon.trade.strategy.TradeSummary;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author zygon
 */
public class InformationModule extends Module {

    private final DataModule dataModule;
    private final InformationManager infoMgmt;
    
    public InformationModule(String name, DataModule dataModule, InformationManager infoMgmt) {
        super(name);
        
        this.dataModule = dataModule;
        this.infoMgmt = infoMgmt;
    }

    @Override
    public Module[] getModules() {
        return new Module[]{this.dataModule};
    }

    public TradeSummary[] getAgentSummaries() {
        List<TradeSummary> summaries = new ArrayList<>();
        
        
        for (TradeAgent trader : this.infoMgmt.getTradeAgents()) {
            summaries.add(trader.getAgentSummary());
        }
        
        return summaries.toArray(new TradeSummary[summaries.size()]);
    }

    public Collection<TradeAgent> getAgents() {
        return this.infoMgmt.getTradeAgents();
    }
    
    @Override
    public void initialize() {
        this.infoMgmt.initialize();
        this.dataModule.getDataManager().setInfoHandler(this.infoMgmt);
    }

    @Override
    public void uninitialize() {
        this.infoMgmt.uninitialize();
        this.dataModule.getDataManager().setInfoHandler(null);
    }
}
