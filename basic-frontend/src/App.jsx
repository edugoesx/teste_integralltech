import { useState, useEffect } from 'react';
import './App.css'; 

export default function App() {
  // Estados para guardar as informações na tela
  const [chamados, setChamados] = useState([]);
  const [analiseIa, setAnaliseIa] = useState(null);
  const [carregandoIa, setCarregandoIa] = useState(false);

  // Estado para o formulário de criação
  const [form, setForm] = useState({
    titulo: '', descricao: '', setor: 'TI', prioridade: 'BAIXA', solicitante: ''
  });

  // O useEffect roda uma vez quando a tela carrega para buscar os chamados
  useEffect(() => {
    carregarChamados();
  }, []);

  // 1. Função para LISTAR chamados
  const carregarChamados = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/chamados');
      const data = await response.json();
      setChamados(data);
    } catch (error) {
      console.error("Erro ao buscar chamados:", error);
    }
  };

  // 2. Função para CRIAR chamado
  const criarChamado = async (e) => {
    e.preventDefault(); // Evita que a página recarregue
    try {
      const response = await fetch('http://localhost:8080/api/chamados', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form)
      });

      if (response.ok) {
        carregarChamados(); // Atualiza a lista
        // Limpa o formulário
        setForm({ titulo: '', descricao: '', setor: 'TI', prioridade: 'BAIXA', solicitante: '' });
      } else {
        alert("Erro ao criar chamado. Verifique os dados.");
      }
    } catch (error) {
      console.error("Erro ao criar chamado:", error);
    }
  };

  // 3. Função para ANALISAR com IA
  const analisarComIa = async (id) => {
    setCarregandoIa(true);
    setAnaliseIa(null); // Limpa a análise anterior, se houver
    
    try {
      const response = await fetch(`http://localhost:8080/api/chamados/${id}/analisar`, {
        method: 'POST'
      });
      const data = await response.json();
      setAnaliseIa(data);
    } catch (error) {
      alert("Erro ao conectar com a IA.");
    } finally {
      setCarregandoIa(false);
    }
  };

  return (
    <div style={{ padding: '20px', fontFamily: 'Arial' }}>
      <h1>Gerenciamento de Chamados 🛠️</h1>

      {/* RESULTADO DA IA (Aparece no topo quando existir) */}
      {carregandoIa && <p>🤖 A Inteligência Artificial está analisando o chamado... aguarde.</p>}
      
      {analiseIa && (
        <div style={{ background: '#e0f7fa', padding: '15px', borderRadius: '8px', marginBottom: '20px' }}>
          <h3>Análise da IA (Chamado #{analiseIa.chamadoId})</h3>
          <p><strong>Setor Sugerido:</strong> {analiseIa.setorSugerido}</p>
          <p><strong>Prioridade Sugerida:</strong> {analiseIa.prioridadeSugerida}</p>
          <p><strong>Resumo:</strong> {analiseIa.resumo}</p>
          <button onClick={() => setAnaliseIa(null)}>Fechar Análise</button>
        </div>
      )}

      {/* FORMULÁRIO DE CRIAÇÃO */}
      <div style={{ border: '1px solid #ccc', padding: '20px', marginBottom: '20px' }}>
        <h2>Abrir Novo Chamado</h2>
        <form onSubmit={criarChamado} style={{ display: 'flex', flexDirection: 'column', gap: '10px', maxWidth: '400px' }}>
          <input 
            type="text" placeholder="Título (mín. 5 caracteres)" value={form.titulo} required
            onChange={e => setForm({...form, titulo: e.target.value})} 
          />
          <input 
            type="text" placeholder="Nome do Solicitante" value={form.solicitante} required
            onChange={e => setForm({...form, solicitante: e.target.value})} 
          />
          <textarea 
            placeholder="Descrição detalhada" value={form.descricao} required
            onChange={e => setForm({...form, descricao: e.target.value})} 
          />
          <select value={form.setor} onChange={e => setForm({...form, setor: e.target.value})}>
            <option value="TI">TI</option>
            <option value="MANUTENCAO">MANUTENÇÃO</option>
            <option value="RH">RH</option>
            <option value="FINANCEIRO">FINANCEIRO</option>
          </select>
          <select value={form.prioridade} onChange={e => setForm({...form, prioridade: e.target.value})}>
            <option value="BAIXA">BAIXA</option>
            <option value="MEDIA">MÉDIA</option>
            <option value="ALTA">ALTA</option>
            <option value="CRITICA">CRÍTICA</option>
          </select>
          <button type="submit">Salvar Chamado</button>
        </form>
      </div>

      {/* LISTAGEM DE CHAMADOS */}
      <h2>Chamados Abertos</h2>
      <div style={{ display: 'flex', gap: '15px', flexWrap: 'wrap' }}>
        {chamados.map(chamado => (
          <div key={chamado.id} style={{ border: '1px solid #333', padding: '15px', borderRadius: '5px', width: '250px' }}>
            <h4>#{chamado.id} - {chamado.titulo}</h4>
            <p><strong>Setor:</strong> {chamado.setor}</p>
            <p><strong>Status:</strong> {chamado.status}</p>
            <p><strong>Solicitante:</strong> {chamado.solicitante}</p>
            <button 
              onClick={() => analisarComIa(chamado.id)}
              disabled={carregandoIa}
            >
              ✨ Analisar com IA
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}