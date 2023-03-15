package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WizardService {
    private final WizardRepository wizardRepository;

    public WizardService(WizardRepository wizardRepository) {
        this.wizardRepository = wizardRepository;
    }

    public List<Wizard> findAllWizards() {
        List<Wizard> wizards = this.wizardRepository.findAll();

        return wizards;
    }

    public Wizard findWizardById(Integer wizardId) {

        return this.wizardRepository
                .findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));

    }

    public Wizard addWizard(Wizard wizard) {
        return this.wizardRepository.save(wizard);
    }

    public Wizard updateWizardById(Integer id, Wizard updatedWizard) {

        return this.wizardRepository.findById(id).map(existedWizard -> {

            existedWizard.setName(updatedWizard.getName());

            return this.wizardRepository.save(existedWizard);
        }).orElseThrow(() -> new ObjectNotFoundException("wizard", id));

    }

    public void deleteWizardById(Integer wizardId) {
        Wizard w = this.wizardRepository
                .findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));

        // remove the owner of artifact belongs to w
        // if not it will cause error as artifact already have foreign key refer to w by ID
        w.deleteArtifacts();

        this.wizardRepository.deleteById(wizardId);
    }
}
