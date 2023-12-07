package org.example.service;


import org.example.dto.UsuarioDTOInput;
import org.example.dto.UsuarioDTOOutput;
import org.example.model.Usuario;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioService {
    private final List<Usuario> usuarioList = new ArrayList<>();
    private final ModelMapper modelMapper = new ModelMapper();

    public Collection<UsuarioDTOOutput> listarUsuarios(){
        return usuarioList.stream().map(usuario -> modelMapper.map(usuario, UsuarioDTOOutput.class)).collect(Collectors.toList());
    }

    public void adicionarUsuario(UsuarioDTOInput input){
        usuarioList.add(modelMapper.map(input, Usuario.class));
    }

    public void alterarUsuario(UsuarioDTOInput input){
        Usuario usuarioEdit = modelMapper.map(input, Usuario.class);
        for (int i = 0; i < usuarioList.size(); i++){
            if(usuarioList.get(i).getId() == usuarioEdit.getId())
                usuarioList.set(i,usuarioEdit);
        }
    }

    public UsuarioDTOOutput buscarUsuario(int id){
        return modelMapper.map(usuarioList.stream().filter(usuario -> usuario.getId()==id).findAny().orElse(null),UsuarioDTOOutput.class);
    }

    public void excluirUsuario(int id){
        usuarioList.removeIf( usuario -> usuario.getId() == id);
    }
}
