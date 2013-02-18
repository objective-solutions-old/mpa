package mpa.manager.control;

import static java.util.Collections.reverse;
import static java.util.Collections.sort;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mpa.core.container.CoreContainerFactory;
import mpa.mad.Mad;
import mpa.mad.MadHome;
import mpa.main.loader.ConfigFiles;
import mpa.main.loader.ExpectedLoader;
import mpa.main.loader.TeamLoader;
import mpa.manager.Connector;
import mpa.manager.bean.Mesa;
import mpa.manager.bean.MpaConfiguracao;
import mpa.manager.bean.Objectiviano;
import mpa.manager.repository.MesaRepository;
import mpa.manager.repository.MpaConfiguracaoRepository;
import mpa.manager.repository.ObjectivianoRepository;
import mpa.pair.Pair;
import mpa.pair.PairHome;
import mpa.scenary.Scenary;
import mpa.scenary.ScenaryHome;
import mpa.search.ScenarySearch;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;

public class MpaControl {

    private static List<Objectiviano> objectivianos;
    private static List<MpaConfiguracao> mpaConfiguracoes;

    public MpaControl() {
        try {
            MesaRepository.createInstance(Connector.getConnection());
            ObjectivianoRepository.createInstance(Connector.getConnection());
            MpaConfiguracaoRepository.createInstance(Connector.getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Mesa> getMesas(MpaConfiguracao mpa) throws SQLException {
        MesaRepository repository = MesaRepository.getInstance();
        List<Mesa> mesas = repository.todos(mpa);
        Collections.sort(mesas);

        return mesas;
    }
    
    public String getDevs(MpaConfiguracao mpa) throws SQLException {
    	String devs = "";
    	if (mpa != null)
    		for (Mesa mesa : getMesas(mpa))
    			devs += mesa.getDevs() + "\n";
    	
    	return devs;
    }

    public List<Objectiviano> getObjectivianos() {
        if (objectivianos == null) {
            ObjectivianoRepository repository = ObjectivianoRepository.getInstance();
            objectivianos = repository.todos();
            Collections.sort(objectivianos);
        }
        return objectivianos;
    }

    public void updateMesa(Mesa mesa) throws SQLException {
        MesaRepository repository = MesaRepository.getInstance();
        repository.update(mesa);
    }

    public Objectiviano objectivianoSelecionado(Objectiviano selected) {
        if (selected == null)
            return selected;
        for (Objectiviano objectiviano : objectivianos)
            if (objectiviano.equals(selected))
                return objectiviano;
        return null;
    }

    public void criaMpaComMesas(Date dataInicio, Date dataFim, String dadosMesas) {
        try {
            validaDadosEntrada(dadosMesas);
            MpaConfiguracao mpaConfiguracao = criaMpaConfiguracao(dataInicio, dataFim);
            criaMesas(mpaConfiguracao, dadosMesas);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void validaDadosEntrada(String dadosMesas) throws SQLException {
        for (String mesaString : quebraString("\n", dadosMesas)) {
            Pattern pattern = Pattern.compile("\\w+( / \\w+)?");
            Matcher matcher = pattern.matcher(mesaString);

            if (!matcher.find())
                throw new IllegalArgumentException("Formato inválido para mesa. (utilize: Dev1 / Dev2)");

            String devsSeparados[] = quebraString(" / ", mesaString);
            ObjectivianoRepository devRepository = ObjectivianoRepository.getInstance();

            devRepository.getObjectiviano(devsSeparados[0]);

            if (devsSeparados.length != 1)
                devRepository.getObjectiviano(devsSeparados[1]);
        }
    }

    private MpaConfiguracao criaMpaConfiguracao(Date dataInicio, Date dataFim) throws SQLException {
        MpaConfiguracao mpaConfiguracao = new MpaConfiguracao(dataInicio, dataFim);
        MpaConfiguracaoRepository mpaConfiguracaorepository = MpaConfiguracaoRepository.getInstance();
        mpaConfiguracaorepository.insert(mpaConfiguracao);
        return mpaConfiguracao;
    }

    private void criaMesas(MpaConfiguracao mpa, String mesasString) throws SQLException {

        ObjectivianoRepository devRepository = ObjectivianoRepository.getInstance();

        int numeroMesa = 1;
        for (String mesaString : quebraString("\n", mesasString)) {
            String devsSeparados[] = quebraString(" / ", mesaString);

            Objectiviano primeiroObjectiviano = devRepository.getObjectiviano(devsSeparados[0].trim());
            Objectiviano segundoObjectiviano = null;

            if (devsSeparados.length != 1)
                segundoObjectiviano = devRepository.getObjectiviano(devsSeparados[1].trim());

            criaMesa(mpa, numeroMesa++, primeiroObjectiviano, segundoObjectiviano);
        }
    }

    public void criaMesa(MpaConfiguracao mpa, int numero, Objectiviano primeiroObjectiviano,
            Objectiviano segundoObjectiviano) throws SQLException {
        MesaRepository mesaRepository = MesaRepository.getInstance();
        Mesa mesa = new Mesa(numero, mpa, primeiroObjectiviano, segundoObjectiviano);
        mesaRepository.insert(mesa);
    }

    private String[] quebraString(String separador, String entrada) {
        return entrada.split(separador);
    }

    public List<MpaConfiguracao> getMpasDisponiveis() {
        MpaConfiguracaoRepository mpaConfiguracaorepository = MpaConfiguracaoRepository.getInstance();
        List<MpaConfiguracao> mpas = mpaConfiguracaorepository.todos();

        sort(mpas);
        reverse(mpas);
        mpaConfiguracoes = mpas;
        
        return mpas;
    }

    public MpaConfiguracao getMpaAtual() {
        for (MpaConfiguracao mpa : mpaConfiguracoes)
            if (mpa.isAtual())
                return mpa;
        return null;
    }

    public MpaConfiguracao getMaiorMpa() {
        return mpaConfiguracoes.get(0);
    }

    public void excluiMesa(Mesa mesa) throws SQLException {
        MesaRepository mesaRepository = MesaRepository.getInstance();
        mesaRepository.delete(mesa);
    }

    public void fechaConexao() {
        Connector.closeConnection();
    }

    public void atualizaNumeroDasMesas(Mesa mesa) throws SQLException {
        MesaRepository mesaRepository = MesaRepository.getInstance();
        mesaRepository.atualizaNumerosDeMesa(mesa);
    }
    
    public String gerarNovoMpa() throws SQLException {
        
        MutablePicoContainer container = CoreContainerFactory.buildContainer();
        ScenarySearch searcher = loadData(container);
        
        container.addComponent(ConfigFiles.class);
        
        container.addComponent(TeamLoader.class);
        container.getComponent(TeamLoader.class).load();
        
        container.addComponent(ExpectedLoader.class);
        container.getComponent(ExpectedLoader.class).load();
        
        Scenary scenary = searcher.search();
        
        StringBuilder builder = new StringBuilder();
        for(Pair p: scenary.getPairs())
            builder.append(p).append("\n");    
        return builder.toString();
    }

    private ScenarySearch loadData(PicoContainer container) throws SQLException {
        ScenarySearch searcher = container.getComponent(ScenarySearch.class);
        MadHome madHome = container.getComponent(MadHome.class);
        PairHome pairHome = container.getComponent(PairHome.class);
        
        for(Objectiviano obj: ObjectivianoRepository.getInstance().todos())
            madHome.createMad(obj.getNome(), new Date());
        
        Mad sozinho = madHome.createMad("Sozinho", new Date());
        
        ScenaryHome scenaryHome = container.getComponent(ScenaryHome.class);
        List<MpaConfiguracao> mpas = getMpasDisponiveis();
        reverse(mpas);
        int c = 1;
        for(MpaConfiguracao mpa: mpas){
            Scenary scenary = scenaryHome.createScenary(c++, mpa.getDataInicio());
            for(Mesa m: MesaRepository.getInstance().todos(mpa)) {
                Mad mad1 = madHome.getByName(m.getPrimeiroObjectiviano().getNome());
                Mad mad2 = m.getSegundoObjectiviano() == null ? sozinho : madHome.getByName(m.getSegundoObjectiviano().getNome());
                scenary.addPair(pairHome.createPair(mad1, mad2));
            }
        }
        return searcher;
    }

}
